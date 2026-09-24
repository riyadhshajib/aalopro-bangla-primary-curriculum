#include <jni.h>

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_ai_needle_AloNeedle3Native_loadAndInit(JNIEnv* env, jobject, jobject, jstring, jstring) {
    return env->NewStringUTF("Needle 3 requires a 64-bit ARM device");
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_ai_needle_AloNeedle3Native_complete(JNIEnv* env, jobject, jstring, jint) {
    return env->NewStringUTF("{\"success\":false,\"error\":\"Needle 3 unavailable on 32-bit ARM\"}");
}

extern "C" JNIEXPORT void JNICALL
Java_com_example_ai_needle_AloNeedle3Native_reset(JNIEnv*, jobject) {}
