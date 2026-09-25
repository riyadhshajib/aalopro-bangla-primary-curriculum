package com.example.ui.screens

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.theme.AaloWordmark
import com.example.ui.theme.WarmPaperBg
import com.example.ui.theme.WarmTextPrimary
import com.example.ui.theme.WarmTextSecondary
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit

private val RegistrationTeal = Color(0xFF2B8D80)
private val RegistrationGold = Color(0xFFE8A900)

/** First-run registration shown immediately after splash. PII is never sent to Analytics. */
@Composable
fun AaloOnboardingScreen(onGetStarted: (String, String) -> Unit, modifier: Modifier = Modifier) {
    val activity = LocalContext.current.findActivity()
    var childName by rememberSaveable { mutableStateOf("") }
    var mobile by rememberSaveable { mutableStateOf("") }
    var otp by rememberSaveable { mutableStateOf("") }
    var verificationId by rememberSaveable { mutableStateOf<String?>(null) }
    var busy by rememberSaveable { mutableStateOf(false) }
    var error by rememberSaveable { mutableStateOf<String?>(null) }

    fun finishRegistration(credential: PhoneAuthCredential) {
        val safeName = childName.trim().take(40)
        val phone = normalizeBangladeshPhone(mobile) ?: return
        busy = true
        error = null
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid
                if (uid == null) {
                    busy = false
                    error = "রেজিস্ট্রেশন শেষ করা যায়নি। আবার চেষ্টা করুন।"
                    return@addOnSuccessListener
                }
                val profile = mapOf(
                    "childName" to safeName,
                    "guardianPhone" to phone,
                    "phoneVerified" to true,
                    "app" to "AaloPro Bangla Pri",
                    "updatedAt" to Timestamp.now()
                )
                FirebaseFirestore.getInstance().collection("users").document(uid).set(profile)
                    .addOnSuccessListener { busy = false; onGetStarted(safeName, phone) }
                    .addOnFailureListener {
                        busy = false
                        error = "নম্বর যাচাই হয়েছে, কিন্তু প্রোফাইল সংরক্ষণ হয়নি। ইন্টারনেট দেখে আবার চেষ্টা করুন।"
                    }
            }
            .addOnFailureListener {
                busy = false
                error = "OTP সঠিক নয় বা মেয়াদ শেষ হয়েছে। আবার চেষ্টা করুন।"
            }
    }

    fun sendOtp() {
        val phone = normalizeBangladeshPhone(mobile)
        when {
            childName.trim().length < 2 -> error = "শিশুর নাম লিখুন।"
            phone == null -> error = "সঠিক ১১ সংখ্যার মোবাইল নম্বর লিখুন।"
            activity == null -> error = "OTP চালু করা যায়নি। অ্যাপটি আবার খুলুন।"
            else -> {
                busy = true
                error = null
                val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) = finishRegistration(credential)
                    override fun onVerificationFailed(exception: com.google.firebase.FirebaseException) {
                        busy = false
                        error = exception.localizedMessage ?: "OTP পাঠানো যায়নি। কিছুক্ষণ পর আবার চেষ্টা করুন।"
                    }
                    override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                        busy = false
                        verificationId = id
                    }
                }
                PhoneAuthProvider.verifyPhoneNumber(
                    PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber(phone).setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(activity).setCallbacks(callbacks).build()
                )
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFFFF8E8), WarmPaperBg, Color(0xFFF3FAF6))))
            .statusBarsPadding().verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 22.dp).testTag("aalo_registration_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painterResource(R.drawable.aalo_brand_logo_soft), "AaloPro লোগো",
            Modifier.size(76.dp).clip(CircleShape))
        Spacer(Modifier.height(10.dp))
        Text("AaloPro", style = MaterialTheme.typography.headlineLarge, fontFamily = AaloWordmark,
            fontWeight = FontWeight.Black, color = WarmTextPrimary)
        Spacer(Modifier.height(14.dp))
        Text("অভিভাবকের সাহায্যে\nশেখা শুরু করি", style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold, color = WarmTextPrimary, textAlign = TextAlign.Center)
        Spacer(Modifier.height(8.dp))
        Text("শিশুর জন্য একটি নিরাপদ শেখার প্রোফাইল তৈরি করতে নিচের তথ্য দিন।",
            style = MaterialTheme.typography.bodyMedium, color = WarmTextSecondary, textAlign = TextAlign.Center)
        Spacer(Modifier.height(14.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StepMark(verificationId == null)
            StepMark(verificationId != null)
        }
        Spacer(Modifier.height(22.dp))

        if (verificationId == null) {
            RegistrationLabel("শিশুর নাম")
            OutlinedTextField(childName, { childName = it.take(40); error = null },
                placeholder = { Text("নাম বা ডাকনাম লিখুন") }, singleLine = true,
                modifier = Modifier.fillMaxWidth().testTag("registration_child_name"),
                leadingIcon = { Icon(Icons.Filled.Person, null, tint = RegistrationGold) },
                shape = RoundedCornerShape(18.dp), colors = registrationFieldColors())
            Spacer(Modifier.height(16.dp))
            RegistrationLabel("অভিভাবকের মোবাইল নম্বর")
            OutlinedTextField(mobile, { mobile = it.filter(Char::isDigit).take(11); error = null },
                prefix = { Text("+880  ", fontWeight = FontWeight.Bold) },
                placeholder = { Text("1XXXXXXXXX") }, singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth().testTag("registration_guardian_phone"),
                shape = RoundedCornerShape(18.dp), colors = registrationFieldColors())
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Filled.CheckCircle, null, tint = RegistrationTeal, modifier = Modifier.size(20.dp))
                Text("এই নম্বরে OTP পাঠিয়ে অভিভাবককে যাচাই করা হবে। শিশুর নিজের নম্বর লাগবে না।",
                    style = MaterialTheme.typography.bodySmall, color = WarmTextSecondary)
            }
            Text("রেজিস্ট্রেশন করলে নম্বরটি যাচাই ও অ্যাকাউন্ট সুরক্ষার জন্য Google Firebase-এ পাঠানো ও সংরক্ষণে সম্মতি দেওয়া হবে।",
                style = MaterialTheme.typography.labelSmall, color = WarmTextSecondary,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
        } else {
            RegistrationLabel("OTP লিখুন")
            Text("${normalizeBangladeshPhone(mobile)} নম্বরে পাঠানো ৬ সংখ্যার কোড দিন।",
                style = MaterialTheme.typography.bodyMedium, color = WarmTextSecondary,
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp))
            OutlinedTextField(otp, { otp = it.filter(Char::isDigit).take(6); error = null },
                placeholder = { Text("••••••") }, singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                modifier = Modifier.fillMaxWidth().testTag("registration_otp"),
                shape = RoundedCornerShape(18.dp), colors = registrationFieldColors())
        }

        error?.let {
            Spacer(Modifier.height(10.dp))
            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth())
        }
        Spacer(Modifier.height(18.dp))
        Button(onClick = {
            if (verificationId == null) sendOtp()
            else if (otp.length == 6) finishRegistration(PhoneAuthProvider.getCredential(verificationId!!, otp))
            else error = "৬ সংখ্যার OTP লিখুন।"
        }, enabled = !busy, modifier = Modifier.fillMaxWidth().height(56.dp).testTag("registration_continue_button"),
            colors = ButtonDefaults.buttonColors(containerColor = RegistrationTeal), shape = RoundedCornerShape(18.dp)) {
            if (busy) CircularProgressIndicator(Modifier.size(22.dp), color = Color.White, strokeWidth = 2.dp)
            else {
                Text(if (verificationId == null) "রেজিস্ট্রেশন করুন" else "OTP যাচাই করুন",
                    style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Icon(Icons.AutoMirrored.Filled.ArrowForward, null, modifier = Modifier.padding(start = 10.dp))
            }
        }
        Spacer(Modifier.height(22.dp))
        Surface(color = Color(0xFFFFF1C9), shape = RoundedCornerShape(20.dp), shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()) {
            Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Person, null, tint = RegistrationGold, modifier = Modifier.size(34.dp))
                Column {
                    Text("নিরাপদ শেখার প্রোফাইল", fontWeight = FontWeight.Bold, color = WarmTextPrimary)
                    Text("যাচাই করা নম্বরের সঙ্গে শিশুর নাম ও শেখার প্রোফাইল নিরাপদে যুক্ত থাকবে।",
                        style = MaterialTheme.typography.bodySmall, color = WarmTextSecondary)
                }
            }
        }
    }
}

@Composable private fun StepMark(active: Boolean) = Box(Modifier.size(50.dp, 5.dp).background(
    if (active) RegistrationGold else Color(0xFFE3D8BD), CircleShape))

@Composable private fun RegistrationLabel(value: String) = Text(value,
    style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = WarmTextPrimary,
    modifier = Modifier.fillMaxWidth().padding(bottom = 7.dp))

@Composable private fun registrationFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = RegistrationTeal, unfocusedBorderColor = Color(0xFFE7D7B5),
    focusedContainerColor = Color.White.copy(alpha = 0.82f), unfocusedContainerColor = Color.White.copy(alpha = 0.72f))

private fun normalizeBangladeshPhone(raw: String): String? {
    val digits = raw.filter(Char::isDigit)
    val local = when {
        digits.length == 11 && digits.startsWith("01") -> digits.drop(1)
        digits.length == 10 && digits.startsWith("1") -> digits
        digits.length == 13 && digits.startsWith("8801") -> digits.drop(3)
        else -> return null
    }
    return if (local.length == 10 && local.startsWith("1")) "+880$local" else null
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
