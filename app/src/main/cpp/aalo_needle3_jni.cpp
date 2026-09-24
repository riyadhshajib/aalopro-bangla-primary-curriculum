#include <jni.h>
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>
#include <mutex>
#include <string>
#include <vector>
#include "needle3/needle.h"

namespace {
std::mutex g_mutex;
std::vector<unsigned char> g_model;

std::string to_utf8(JNIEnv* env, jstring value) {
    if (!value) return {};
    const char* chars = env->GetStringUTFChars(value, nullptr);
    std::string result(chars ? chars : "");
    if (chars) env->ReleaseStringUTFChars(value, chars);
    return result;
}
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_ai_needle_AloNeedle3Native_loadAndInit(
    JNIEnv* env, jobject, jobject asset_manager, jstring system_prompt, jstring tools_json) {
    std::lock_guard<std::mutex> lock(g_mutex);
    AAssetManager* manager = AAssetManager_fromJava(env, asset_manager);
    if (!manager) return env->NewStringUTF("asset manager unavailable");
    AAsset* asset = AAssetManager_open(manager, "needle3/needle3.cact", AASSET_MODE_BUFFER);
    if (!asset) return env->NewStringUTF("needle3 model asset missing");
    const auto size = static_cast<size_t>(AAsset_getLength64(asset));
    g_model.resize(size);
    const int read = AAsset_read(asset, g_model.data(), size);
    AAsset_close(asset);
    if (read < 0 || static_cast<size_t>(read) != size) return env->NewStringUTF("needle3 model read failed");
    if (needle_load(g_model.data(), static_cast<unsigned long long>(g_model.size())) != 0) {
        const char* error = needle_last_error();
        return env->NewStringUTF(error ? error : "needle3 load failed");
    }
    const std::string system = to_utf8(env, system_prompt);
    const std::string tools = to_utf8(env, tools_json);
    if (needle_init(system.c_str(), tools.c_str(), nullptr) != 0) {
        const char* error = needle_last_error();
        return env->NewStringUTF(error ? error : "needle3 init failed");
    }
    return nullptr;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_ai_needle_AloNeedle3Native_complete(JNIEnv* env, jobject, jstring input, jint max_tokens) {
    std::lock_guard<std::mutex> lock(g_mutex);
    const std::string text = to_utf8(env, input);
    std::vector<char> output(32768, '\0');
    if (needle_complete(text.c_str(), max_tokens, output.data(), static_cast<int>(output.size())) != 0) {
        const char* error = needle_last_error();
        std::string result = std::string("{\"success\":false,\"error\":\"") + (error ? error : "completion failed") + "\"}";
        return env->NewStringUTF(result.c_str());
    }
    return env->NewStringUTF(output.data());
}

extern "C" JNIEXPORT void JNICALL
Java_com_example_ai_needle_AloNeedle3Native_reset(JNIEnv*, jobject) {
    std::lock_guard<std::mutex> lock(g_mutex);
    needle_reset();
}
