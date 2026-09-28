package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.annotation.DrawableRes
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.profile.ChildProfile
import com.example.data.profile.CURRENT_CONSENT_VERSION
import com.example.data.profile.ParentProfile
import com.example.data.profile.ProfileLanguage
import com.example.ui.theme.AaloWordmark
import com.example.ui.theme.WarmTextPrimary
import com.example.ui.theme.WarmTextSecondary

private val ProfileTeal = Color(0xFF2B8D80)
private val ProfileGold = Color(0xFFF4B72D)
private val ProfileCream = Color(0xFFFFFBF3)
private val ProfileMint = Color(0xFFE4F4EE)
private val ProfilePeach = Color(0xFFFFE9D7)
private val ProfileSky = Color(0xFFE8F3F8)

data class OnboardingProfileDraft(
    val parent: ParentProfile,
    val child: ChildProfile,
    val consentGiven: Boolean,
)

private data class AvatarOption(
    val id: String,
    @DrawableRes val drawable: Int,
    val label: String,
)

private val avatarOptions = listOf(
    AvatarOption("learner_1", R.drawable.rank_little_hero_3d, "লিটল হিরো"),
    AvatarOption("learner_2", R.drawable.rank_power_explorer_3d, "পাওয়ার এক্সপ্লোরার"),
    AvatarOption("learner_3", R.drawable.rank_brain_hero_3d, "ব্রেইন হিরো"),
    AvatarOption("learner_4", R.drawable.rank_aalo_champion_3d, "আলো চ্যাম্পিয়ন"),
)

/** Warm, portrait-first profile creation flow shown immediately after the splash screen. */
@Composable
fun AaloOnboardingScreen(
    onComplete: (OnboardingProfileDraft) -> Unit,
    modifier: Modifier = Modifier,
    initialConsent: Boolean = false,
    initialParent: ParentProfile? = null,
    initialChild: ChildProfile? = null,
    onExit: (() -> Unit)? = null,
) {
    var step by rememberSaveable { mutableIntStateOf(1) }
    var guardianName by rememberSaveable { mutableStateOf(initialParent?.displayName.orEmpty()) }
    var mobile by rememberSaveable { mutableStateOf(initialParent?.guardianPhone.orEmpty()) }
    var consent by rememberSaveable { mutableStateOf(initialConsent || (initialParent?.consentGivenAtEpochMs ?: 0L) > 0L) }
    var childName by rememberSaveable { mutableStateOf(initialChild?.displayName.orEmpty()) }
    var classLevel by rememberSaveable { mutableIntStateOf(initialChild?.classNumber ?: 1) }
    var avatarId by rememberSaveable { mutableStateOf(initialChild?.avatarId ?: ChildProfile.DEFAULT_AVATAR_ID) }
    var language by rememberSaveable { mutableStateOf(initialChild?.language ?: ProfileLanguage.BANGLA) }
    var error by rememberSaveable { mutableStateOf<String?>(null) }

    BackHandler(enabled = step > 1 || onExit != null) {
        if (step > 1) step-- else onExit?.invoke()
        error = null
    }

    Box(modifier.fillMaxSize().background(
        Brush.verticalGradient(listOf(Color(0xFFFFF5D9), ProfileCream, Color(0xFFF0F8F4)))
    )) {
        Box(Modifier.align(Alignment.TopEnd).size(130.dp).clip(RoundedCornerShape(bottomStart = 100.dp))
            .background(Brush.radialGradient(listOf(ProfileGold.copy(alpha = .42f), Color.Transparent))))
        Column(
            Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().imePadding()
                .verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 18.dp)
                .testTag("aalo_profile_creation"),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ProfileHeader(step, when {
                step > 1 -> ({ step--; error = null })
                onExit != null -> onExit
                else -> null
            })
            Spacer(Modifier.height(18.dp))
            when (step) {
                1 -> GuardianStep(guardianName, { guardianName = it.take(40); error = null }, mobile,
                    { mobile = it.filter(Char::isDigit).take(13); error = null }, consent,
                    { consent = it; error = null },
                    error) {
                    when {
                        guardianName.trim().length < 2 -> error = "অভিভাবকের নাম লিখুন।"
                        normalizeBangladeshPhone(mobile) == null -> error = "সঠিক মোবাইল নম্বর লিখুন।"
                        !consent -> error = "সম্মতি দিলে পরের ধাপে যেতে পারবেন।"
                        else -> { error = null; step = 2 }
                    }
                }
                2 -> ChildStep(childName, { childName = it.take(40); error = null }, classLevel,
                    { classLevel = it }, avatarId, { avatarId = it }, language, { language = it }, error) {
                    if (childName.trim().length < 2) error = "শিশুর নাম বা ডাকনাম লিখুন।"
                    else { error = null; step = 3 }
                }
                else -> SuccessStep(childName.trim(), classLevel, avatarId) {
                    onComplete(OnboardingProfileDraft(
                        parent = ParentProfile(
                            id = initialParent?.id ?: "local-parent",
                            displayName = guardianName.trim(),
                            guardianPhone = normalizeBangladeshPhone(mobile).orEmpty(),
                            phoneVerified = initialParent?.phoneVerified ?: false,
                            consentVersion = CURRENT_CONSENT_VERSION,
                            consentGivenAtEpochMs = System.currentTimeMillis(),
                        ),
                        child = ChildProfile(
                            id = initialChild?.id.orEmpty(),
                            displayName = childName.trim(),
                            classNumber = classLevel,
                            avatarId = avatarId,
                            language = language,
                        ),
                        consentGiven = consent,
                    ))
                }
            }
        }
    }
}

@Composable
private fun ProfileHeader(step: Int, onBack: (() -> Unit)?) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(42.dp), contentAlignment = Alignment.Center) {
            if (onBack != null) IconButton(onClick = onBack, modifier = Modifier.testTag("profile_back")) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "আগের ধাপে যান")
            }
        }
        Row(Modifier.weight(1f), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Image(painterResource(R.drawable.aalo_brand_logo_soft), "AaloPro লোগো", Modifier.size(42.dp).clip(CircleShape))
            Text("AaloPro", fontFamily = AaloWordmark, fontWeight = FontWeight.Black,
                style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(start = 8.dp))
        }
        Spacer(Modifier.size(42.dp))
    }
    Spacer(Modifier.height(14.dp))
    Row(Modifier.fillMaxWidth().testTag("profile_progress"), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(3) { index ->
            Box(Modifier.weight(1f).height(7.dp).clip(CircleShape)
                .background(if (index < step) ProfileGold else Color(0xFFE7DDC9))
                .semantics { contentDescription = "ধাপ ${index + 1}${if (index < step) " চালু" else " বাকি"}" })
        }
    }
    Text("ধাপ $step / ৩", style = MaterialTheme.typography.labelMedium, color = WarmTextSecondary,
        modifier = Modifier.padding(top = 7.dp).testTag("profile_step_label"))
}

@Composable
private fun GuardianStep(
    name: String, onName: (String) -> Unit, phone: String, onPhone: (String) -> Unit,
    consent: Boolean, onConsent: (Boolean) -> Unit, error: String?, onContinue: () -> Unit,
) {
    StepTitle("অভিভাবকের তথ্য", "আপনার সাহায্যেই শিশুর শেখার পথটি নিরাপদ থাকবে।")
    Spacer(Modifier.height(18.dp))
    SoftCard(ProfileMint) {
        FieldLabel("আপনার নাম")
        OutlinedTextField(name, onName, placeholder = { Text("নাম লিখুন") }, singleLine = true,
            leadingIcon = { Icon(Icons.Filled.Person, null, tint = ProfileTeal) },
            modifier = Modifier.fillMaxWidth().testTag("guardian_name"), shape = RoundedCornerShape(18.dp),
            colors = profileFieldColors())
        Spacer(Modifier.height(15.dp))
        FieldLabel("মোবাইল নম্বর")
        OutlinedTextField(phone, onPhone, placeholder = { Text("01XXXXXXXXX") }, singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            leadingIcon = { Icon(Icons.Filled.PhoneAndroid, null, tint = ProfileTeal) },
            modifier = Modifier.fillMaxWidth().testTag("guardian_phone"), shape = RoundedCornerShape(18.dp),
            colors = profileFieldColors())
    }
    Spacer(Modifier.height(14.dp))
    Surface(color = Color.White.copy(alpha = .86f), shape = RoundedCornerShape(18.dp), shadowElevation = 4.dp,
        modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
            Checkbox(consent, onConsent, modifier = Modifier.testTag("guardian_consent"),
                colors = CheckboxDefaults.colors(checkedColor = ProfileTeal))
            Column(Modifier.padding(top = 5.dp)) {
                Text("আমি সম্মতি দিচ্ছি", fontWeight = FontWeight.Bold)
                Text("শিশুর শেখার প্রোফাইল চালাতে নাম ও নম্বর নিরাপদে রাখা হবে।",
                    style = MaterialTheme.typography.bodySmall, color = WarmTextSecondary)
            }
        }
    }
    ErrorText(error)
    PrimaryAction("পরের ধাপ", "guardian_continue", onContinue)
    Row(Modifier.padding(top = 14.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Filled.Lock, null, tint = ProfileTeal, modifier = Modifier.size(16.dp))
        Text("এই তথ্য বিজ্ঞাপনের জন্য ব্যবহার করা হবে না।", style = MaterialTheme.typography.labelSmall,
            color = WarmTextSecondary, modifier = Modifier.padding(start = 6.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChildStep(
    childName: String, onName: (String) -> Unit, classLevel: Int, onClass: (Int) -> Unit,
    avatarId: String, onAvatar: (String) -> Unit,
    language: ProfileLanguage, onLanguage: (ProfileLanguage) -> Unit,
    error: String?, onContinue: () -> Unit,
) {
    val selectedAvatar = avatarOptions.firstOrNull { it.id == avatarId } ?: avatarOptions.first()
    StepTitle("শিশুর শেখার প্রোফাইল", "যেভাবে ডাকেন, সেই নামটাই লিখতে পারেন।")
    Spacer(Modifier.height(18.dp))
    SoftCard(ProfilePeach) {
        FieldLabel("শিশুর নাম বা ডাকনাম")
        OutlinedTextField(childName, onName, placeholder = { Text("যেমন: রিয়া") }, singleLine = true,
            leadingIcon = { Image(painterResource(selectedAvatar.drawable), null, Modifier.size(38.dp)) },
            modifier = Modifier.fillMaxWidth().testTag("child_name"),
            shape = RoundedCornerShape(18.dp), colors = profileFieldColors())
        Spacer(Modifier.height(18.dp))
        FieldLabel("কোন শ্রেণিতে পড়ে?")
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            (1..5).forEach { level ->
                FilterChip(classLevel == level, { onClass(level) }, { Text(level.bn(), fontWeight = FontWeight.Bold) },
                    modifier = Modifier.weight(1f).testTag("class_$level"),
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = ProfileGold))
            }
        }
    }
    Spacer(Modifier.height(14.dp))
    SoftCard(ProfileSky) {
        FieldLabel("একটি ছবি বেছে নাও")
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            avatarOptions.forEach { choice ->
                Surface(color = if (avatarId == choice.id) ProfileGold else Color.White.copy(alpha = .82f),
                    shape = CircleShape, shadowElevation = if (avatarId == choice.id) 8.dp else 2.dp,
                    modifier = Modifier.size(62.dp).clickable { onAvatar(choice.id) }.testTag("avatar_${choice.id}")
                        .semantics { contentDescription = "${choice.label} ছবি" }) {
                    Box(contentAlignment = Alignment.Center) {
                        Image(painterResource(choice.drawable), null, Modifier.size(54.dp))
                    }
                }
            }
        }
        Text("নিজের ছবি পরে যোগ করা যাবে।", style = MaterialTheme.typography.bodySmall,
            color = WarmTextSecondary, modifier = Modifier.padding(top = 10.dp))
        Spacer(Modifier.height(16.dp))
        FieldLabel("অ্যাপের ভাষা")
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            listOf(
                ProfileLanguage.BANGLA to "বাংলা",
                ProfileLanguage.BANGLISH to "Banglish",
                ProfileLanguage.ENGLISH to "English",
            ).forEach { (choice, label) ->
                FilterChip(language == choice, { onLanguage(choice) }, { Text(label) },
                    modifier = Modifier.weight(1f).testTag("language_${choice.storageValue}"),
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = ProfileMint))
            }
        }
    }
    ErrorText(error)
    PrimaryAction("প্রোফাইল তৈরি করি", "child_continue", onContinue)
}

@Composable
private fun SuccessStep(name: String, classLevel: Int, avatarId: String, onStart: () -> Unit) {
    val avatar = avatarOptions.firstOrNull { it.id == avatarId } ?: avatarOptions.first()
    Surface(color = Color.White.copy(alpha = .9f), shape = RoundedCornerShape(32.dp), shadowElevation = 12.dp,
        modifier = Modifier.fillMaxWidth().testTag("profile_success")) {
        Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.size(104.dp).clip(CircleShape).background(ProfileMint).border(4.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center) {
                Image(painterResource(avatar.drawable), avatar.label, Modifier.size(94.dp))
            }
            Spacer(Modifier.height(14.dp))
            Icon(Icons.Filled.CheckCircle, "প্রোফাইল তৈরি হয়েছে", tint = ProfileTeal, modifier = Modifier.size(34.dp))
            Text("সব তৈরি, $name!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center, modifier = Modifier.padding(top = 8.dp))
            Text("${classLevel.bn()}ম শ্রেণির শেখা এখন থেকেই শুরু করা যাবে।",
                color = WarmTextSecondary, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 8.dp))
            Surface(color = Color(0xFFFFF2C9), shape = RoundedCornerShape(18.dp), modifier = Modifier.padding(top = 20.dp)) {
                Text("প্রশ্ন করো • বুঝে শেখো • কুইজ খেলো", fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center, modifier = Modifier.padding(14.dp))
            }
        }
    }
    PrimaryAction("শেখা শুরু করি", "profile_start_learning", onStart)
}

@Composable private fun StepTitle(title: String, subtitle: String) {
    Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
    Text(subtitle, color = WarmTextSecondary, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 7.dp))
}

@Composable private fun SoftCard(color: Color, content: @Composable ColumnScope.() -> Unit) =
    Surface(color = color, shape = RoundedCornerShape(26.dp), shadowElevation = 8.dp, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp), content = content)
    }

@Composable private fun FieldLabel(value: String) = Text(value, fontWeight = FontWeight.Bold, color = WarmTextPrimary,
    modifier = Modifier.fillMaxWidth().padding(bottom = 7.dp))

@Composable private fun ErrorText(value: String?) {
    if (value != null) Text(value, color = MaterialTheme.colorScheme.error,
        modifier = Modifier.fillMaxWidth().padding(top = 12.dp).testTag("profile_error"))
}

@Composable private fun PrimaryAction(text: String, tag: String, onClick: () -> Unit, busy: Boolean = false) {
    Spacer(Modifier.height(18.dp))
    Button(onClick, enabled = !busy, modifier = Modifier.fillMaxWidth().height(58.dp).testTag(tag),
        colors = ButtonDefaults.buttonColors(containerColor = ProfileTeal), shape = RoundedCornerShape(19.dp)) {
        if (busy) CircularProgressIndicator(Modifier.size(23.dp), color = Color.White, strokeWidth = 2.dp)
        else {
            Text(text, fontWeight = FontWeight.Bold)
            Icon(Icons.AutoMirrored.Filled.ArrowForward, null, modifier = Modifier.padding(start = 9.dp))
        }
    }
}

@Composable private fun profileFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = ProfileTeal, unfocusedBorderColor = Color(0xFFD9CBAF),
    focusedContainerColor = Color.White.copy(alpha = .92f), unfocusedContainerColor = Color.White.copy(alpha = .8f))

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

private fun Int.bn(): String = toString().map { ('০'.code + it.code - '0'.code).toChar() }.joinToString("")
