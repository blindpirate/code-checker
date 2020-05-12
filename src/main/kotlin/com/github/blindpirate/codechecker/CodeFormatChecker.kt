@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.github.blindpirate.codechecker

import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap
import com.puppycrawl.tools.checkstyle.Checker
import com.puppycrawl.tools.checkstyle.ConfigurationLoader
import com.puppycrawl.tools.checkstyle.ModuleFactory
import com.puppycrawl.tools.checkstyle.PackageObjectFactory
import com.puppycrawl.tools.checkstyle.PropertiesExpander
import com.puppycrawl.tools.checkstyle.ThreadModeSettings
import com.puppycrawl.tools.checkstyle.api.AuditEvent
import com.puppycrawl.tools.checkstyle.api.AuditListener
import com.puppycrawl.tools.checkstyle.api.CheckstyleException
import com.puppycrawl.tools.checkstyle.api.LocalizedMessage
import com.puppycrawl.tools.checkstyle.api.RootModule
import java.io.File
import java.util.Locale
import java.util.Properties

//fun main() {
//    val listener = MyListener()
//    CodeFormatChecker().runCheckstyle(
//        "/checkstyle.xml",
//        listOf(*File("src/test/resources/bad-format-code").listFiles()),
//        listener)
//
//    LocalizedMessage.setLocale(Locale.CHINESE)
//    listener.fileToErrorsMap.forEach { file: String, message: LocalizedMessage ->
//        println("${file}:${message.lineNo}")
//        println(message.message)
//    }
//}

class CodeFormatChecker {
    fun check(file: File): List<CodeFormatIssue> {
        return emptyList()
    }

    fun runCheckstyle(file: File): Multimap<String, LocalizedMessage> {
        val listener = MyListener()
        CodeFormatChecker().runCheckstyle("/checkstyle.xml", listOf(file), listener)
        return listener.fileToErrorsMap
    }

    fun runCheckstyle(configFileLocation: String, filesToProcess: List<File>, auditListener: AuditListener): Int {
        // create a configuration
        val config = ConfigurationLoader.loadConfiguration(
            configFileLocation, PropertiesExpander(Properties()),
            ConfigurationLoader.IgnoredModulesOptions.OMIT, ThreadModeSettings(1, 1))

        // create RootModule object and run it
        val errorCounter: Int
        val moduleClassLoader = Checker::class.java.classLoader
        val rootModule = getRootModule(config.name, moduleClassLoader)
        try {
            rootModule.setModuleClassLoader(moduleClassLoader)
            rootModule.configure(config)
            rootModule.addListener(auditListener)

            // run RootModule
            errorCounter = rootModule.process(filesToProcess)
        } finally {
            rootModule.destroy()
        }
        return errorCounter
    }

    @Throws(CheckstyleException::class)
    private fun getRootModule(name: String, moduleClassLoader: ClassLoader): RootModule {
        val factory: ModuleFactory = PackageObjectFactory(
            Checker::class.java.getPackage().name, moduleClassLoader)
        return factory.createModule(name) as RootModule
    }

}

enum class CodeIssueType {
    FORMAT,
    NAMING_CONVENTION,
    BAD_PRACTICE,
    CAN_BE_SIMPLIFIED
}

enum class CodeFormatIssueType {
    // https://checkstyle.sourceforge.io/config_blocks.html#EmptyBlock
    CHECKSTYLE_EMPTY_BLOCK,

    // https://checkstyle.sourceforge.io/config_blocks.html#EmptyCatchBlock
    CHECKSTYLE_EMPTY_CATCH_BLOCK,

    // https://checkstyle.sourceforge.io/config_blocks.html#NeedBraces
    // allowSingleLineStatement
    CHECKSTYLE_NEED_BRACES,

    // https://checkstyle.sourceforge.io/config_coding.html#DeclarationOrder
    CHECKSTYLE_DECLARATION_ORDER,

    // https://checkstyle.sourceforge.io/config_coding.html#DefaultComesLast
    CHECKSTYLE_DEFAULT_COMES_LAST,

    // https://checkstyle.sourceforge.io/config_coding.html#EmptyStatement
    CHECKSTYLE_EMPTY_STATEMENT,

    // https://checkstyle.sourceforge.io/config_coding.html#EqualsAvoidNull
    CHECKSTYLE_EQUALS_AVOID_NULL,

    // https://checkstyle.sourceforge.io/config_coding.html#EqualsHashCode
    CHECKSTYLE_EQUALS_HASH_CODE,

    // https://checkstyle.sourceforge.io/config_coding.html#MagicNumber
    CHECKSTYLE_MAGIC_NUMBER,

    // https://checkstyle.sourceforge.io/config_coding.html#MissingSwitchDefault
    CHECKSTYLE_MISSING_SWITCH_DEFAULT,

    // https://checkstyle.sourceforge.io/config_coding.html#NestedForDepth
    CHECKSTYLE_NESTED_FOR_DEPTH,
    CHECKSTYLE_NESTED_IF_DEPTH,
    CHECKSTYLE_NESTED_TRY_DEPTH,

    // https://checkstyle.sourceforge.io/config_coding.html#OneStatementPerLine
    CHECKSTYLE_ONE_STATEMENT_PER_LINE,

    // https://checkstyle.sourceforge.io/config_coding.html#ParameterAssignment
    CHECKSTYLE_PARAMETER_ASSIGNMENT,

    // https://checkstyle.sourceforge.io/config_coding.html#SimplifyBooleanExpression
    CHECKSTYLE_SIMPLIFY_BOOLEAN_EXPRESSION,

    // https://checkstyle.sourceforge.io/config_coding.html#SimplifyBooleanReturn
    CHECKSTYLE_SIMPLIFY_BOOLEAN_RETURN,

    // https://checkstyle.sourceforge.io/config_coding.html#StringLiteralEquality
    CHECKSTYLE_STRING_LITERAL_EQUALITY,

    // https://checkstyle.sourceforge.io/config_coding.html#UnnecessaryParentheses
    CHECKSTYLE_UNNECESSARY_PARENTHESES,

    // https://checkstyle.sourceforge.io/config_coding.html#UnnecessarySemicolonAfterOuterTypeDeclaration
    // https://checkstyle.sourceforge.io/config_coding.html#UnnecessarySemicolonAfterTypeMemberDeclaration
    // https://checkstyle.sourceforge.io/config_coding.html#UnnecessarySemicolonInEnumeration
    // https://checkstyle.sourceforge.io/config_coding.html#UnnecessarySemicolonInTryWithResources
    CHECKSTYLE_UNNECESSARY_SEMICOLON,

    // https://checkstyle.sourceforge.io/config_imports.html#AvoidStarImport
    CHECKSTYLE_AVOID_STAR_IMPORT,
    CHECKSTYLE_REDUNDANT_IMPORT,
    CHECKSTYLE_UNUSED_IMPORT,

    // TODO Code complexity
    // https://checkstyle.sourceforge.io/config_metrics.html#JavaNCSS

    // TODO Indention
    // https://checkstyle.sourceforge.io/config_misc.html#Indentation

    // https://checkstyle.sourceforge.io/config_misc.html#NewlineAtEndOfFile
    CHECKSTYLE_NEWLINE_EOF,

    // https://checkstyle.sourceforge.io/config_naming.html#ConstantName
    CHECKSTYLE_CONSTANT_NAME,

    // https://checkstyle.sourceforge.io/config_naming.html#LambdaParameterName
    CHECKSTYLE_LAMBDA_PARAMETER_NAME,

    // https://checkstyle.sourceforge.io/config_naming.html#LocalVariableName
    CHECKSTYLE_LOCAL_VARIABLE_NAME,

    // https://checkstyle.sourceforge.io/config_naming.html#MemberName
    CHECKSTYLE_MEMBER_NAME,

    // https://checkstyle.sourceforge.io/config_naming.html#MethodName
    CHECKSTYLE_METHOD_NAME,

    // https://checkstyle.sourceforge.io/config_naming.html#PackageName
    CHECKSTYLE_PACKAGE_NAME,

    // https://checkstyle.sourceforge.io/config_naming.html#ParameterName
    CHECKSTYLE_PARAMTER_NAME,

    // https://checkstyle.sourceforge.io/config_naming.html#StaticVariableName
    CHECKSTYLE_STATIC_VARIABLE_NAME,

    // https://checkstyle.sourceforge.io/config_naming.html#TypeName
    CHECKSTYLE_TYPE_NAME,

    // https://checkstyle.sourceforge.io/config_whitespace.html#FileTabCharacter
    CHECKSTYLE_FILE_TAB_CHAR,

    // https://checkstyle.sourceforge.io/config_whitespace.html#GenericWhitespace
    CHECKSTYLE_GENERIC_WHITESPACE,

    // TODO https://checkstyle.sourceforge.io/config_whitespace.html#MethodParamPad
    // https://checkstyle.sourceforge.io/config_whitespace.html#NoLineWrap
    CHECKSTYLE_NO_LINE_WRAP,

    // https://checkstyle.sourceforge.io/config_whitespace.html#WhitespaceAfter
    CHECKSTYLE_WHITESPACE_AFTER,

    // https://checkstyle.sourceforge.io/config_whitespace.html#NoWhitespaceBefore
    CHECKSTYLE_NO_WHITESPACE_BEFORE,

    // https://checkstyle.sourceforge.io/config_whitespace.html#OperatorWrap
    CHECKSTYLE_OPERATOR_WRAP,

    // https://checkstyle.sourceforge.io/config_whitespace.html#ParenPad
    CHECKSTYLE_PAREN_PAD,

    // https://checkstyle.sourceforge.io/config_whitespace.html#SingleSpaceSeparator
    CHECKSTYLE_SINGLE_SPACE_SEPARATOR,

    // https://checkstyle.sourceforge.io/config_whitespace.html#TypecastParenPad
    CHECKSTYLE_TYPECASE_PAREN_PAD,

    // https://checkstyle.sourceforge.io/config_whitespace.html#WhitespaceAround
    CHECKSTYLE_WHITESPACE_AROUND,
    MISSING_SPACES,
    MISSING_CURLY_BRACKETS,
    UNUSED_IMPORT
}

class MyListener : AuditListener {
    val fileToErrorsMap: Multimap<String, LocalizedMessage> = ArrayListMultimap.create()
    override fun addError(event: AuditEvent) {
        if (event.localizedMessage != null) {
            fileToErrorsMap.put(event.fileName, event.localizedMessage)
        }
    }

    override fun fileFinished(event: AuditEvent) {
    }

    override fun auditFinished(event: AuditEvent) {
    }

    override fun addException(event: AuditEvent, throwable: Throwable) {
    }

    override fun fileStarted(event: AuditEvent) {
    }

    override fun auditStarted(event: AuditEvent) {
    }

}

class CodeFormatIssue(val type: CodeFormatIssueType, val startLine: Int, val endLine: Int) {
}