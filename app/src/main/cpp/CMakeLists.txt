cmake_minimum_required(VERSION 3.22.1)
project(aalo_needle3)

find_library(android-lib android)
find_library(log-lib log)
if(ANDROID_ABI STREQUAL "arm64-v8a")
    add_library(needle3_prebuilt STATIC IMPORTED)
    set_target_properties(needle3_prebuilt PROPERTIES
        IMPORTED_LOCATION "${CMAKE_CURRENT_SOURCE_DIR}/needle3/${ANDROID_ABI}/libneedle.a")
    add_library(aalo_needle3 SHARED aalo_needle3_jni.cpp)
    target_include_directories(aalo_needle3 PRIVATE "${CMAKE_CURRENT_SOURCE_DIR}/needle3")
    target_link_libraries(aalo_needle3 needle3_prebuilt ${android-lib} ${log-lib})
else()
    # The upstream armv7 archive currently requires a newer private libc++ symbol.
    # Keep 32-bit devices installable and fall back to AaloPro's deterministic router.
    add_library(aalo_needle3 SHARED aalo_needle3_stub.cpp)
endif()
