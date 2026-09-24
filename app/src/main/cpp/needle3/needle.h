#pragma once

#ifdef __cplusplus
extern "C" {
#endif
int needle_init(const char* system_prompt, const char* tools_json, const char* tool_index_path);
const char* needle_last_error(void);
int needle_complete(const char* input, int max_new_tokens, char* out, int out_capacity);
void needle_reset(void);
int needle_load(const unsigned char* cact, unsigned long long n);
#ifdef __cplusplus
}
#endif
