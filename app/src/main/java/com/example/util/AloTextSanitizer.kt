package com.example.util

/**
 * Utility to sanitize AI responses and educational texts.
 * Strips raw markdown asterisks (**bold**, *italic*), emoji clutter (⭐, 💡, 🔬, etc.),
 * and normalizes formatting into clean, readable, structured Bangla text.
 */
object AloTextSanitizer {

    // Regex to match markdown asterisks (**text**, *text*, etc.)
    private val BOLD_ASTERISK_REGEX = "\\*\\*([^*]+)\\*\\*".toRegex()
    private val ITALIC_ASTERISK_REGEX = "\\*([^*]+)\\*".toRegex()
    private val HASH_HEADER_REGEX = "(?m)^#{1,6}\\s*".toRegex()
    private val BACKTICK_BLOCK_REGEX = "```(?:[a-zA-Z]*\n)?([\\s\\S]*?)```".toRegex()
    private val INLINE_CODE_REGEX = "`([^`]+)`".toRegex()
    // A comma can be part of a textbook answer, for example
    // "Hello, Riya. How are you today?". Do not strip that instructional text.
    private val LEADING_GREETING_REGEX = Regex("^(?:নমস্কার|হ্যালো|আসসালামু আলাইকুম|Namaskar|Namaste|Hello|Hi)[!।.ঃ:]+\\s*", RegexOption.IGNORE_CASE)
    private val LEADING_SELF_INTRO_REGEX = Regex("^আমি\\s+আলোপ্রো(?:\\s*\\(AaloPro\\))?[^।.!\\n]*[।.!]\\s*", RegexOption.IGNORE_CASE)

    // Decorative emojis that cause visual clutter
    private val CLUTTER_EMOJIS_REGEX = "[💡⭐🍎⚡🔬📐✨📌🎓🌍🚀🔥🎯📝⚛️🔍📖🌱🏷️🔘🔹🔸▶️✅❌💬🗣️🤖🧑‍🏫]".toRegex()

    /**
     * Cleans raw text completely: removes decorative emojis, strips markdown syntax (**...**),
     * and normalizes bullet points and spacing into clean, distraction-free text.
     */
    fun cleanText(raw: String): String {
        if (raw.isEmpty()) return ""

        var result = raw.trimStart()

        // Answer cards should begin with the answer, not a model greeting or
        // a repeated introduction. Only sentence-opening greetings are removed;
        // lesson text that discusses a greeting as a word remains intact.
        result = LEADING_GREETING_REGEX.replace(result, "")
        result = LEADING_SELF_INTRO_REGEX.replace(result, "")

        // 1. Remove backtick code blocks (preserve inner content)
        result = BACKTICK_BLOCK_REGEX.replace(result) { match -> match.groupValues[1].trim() }
        result = INLINE_CODE_REGEX.replace(result) { match -> match.groupValues[1] }

        // 2. Remove markdown asterisks (paired and stray asterisks)
        result = BOLD_ASTERISK_REGEX.replace(result) { match -> match.groupValues[1] }
        result = ITALIC_ASTERISK_REGEX.replace(result) { match -> match.groupValues[1] }
        result = result.replace("**", "").replace("*", "")

        // 3. Remove heading hash symbols (### Heading -> Heading)
        result = HASH_HEADER_REGEX.replace(result, "")

        // 4. Remove decorative emojis
        result = CLUTTER_EMOJIS_REGEX.replace(result, "")

        // 5. Clean up redundant spaces around lines and standardize bullet points
        val lines = result.lines().map { line ->
            var l = line.trim()
            if (l.startsWith("- ") || l.startsWith("• ")) {
                l = "• " + l.substring(2).trim()
            }
            l
        }

        // 6. Join lines and eliminate 3+ consecutive newlines
        val joined = lines.joinToString("\n").replace(Regex("\n{3,}"), "\n\n")
        return joined.trim()
    }
}
