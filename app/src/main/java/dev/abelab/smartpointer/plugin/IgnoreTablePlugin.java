package dev.abelab.smartpointer.plugin;

import java.util.List;

import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * MyBatis Generatorで不要なテーブルを無視するプラグイン
 */
public class IgnoreTablePlugin extends PluginAdapter {

    private final List<String> IGNORE_TABLE_PATTERNS = List.of( //
        "flyway_schema_history", //
        "SPRING_SESSION", //
        "SPRING_SESSION_ATTRIBUTES" //
    );

    @Override
    public boolean validate(final List<String> warnings) {
        return true;
    }

    private boolean checkIsTableToGenerate(final IntrospectedTable introspectedTable) {
        final var tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime().replace("`", "");
        return this.IGNORE_TABLE_PATTERNS.stream().noneMatch(tableName::matches);
    }

    @Override
    public boolean modelBaseRecordClassGenerated(final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        return this.checkIsTableToGenerate(introspectedTable);
    }

    @Override
    public boolean modelExampleClassGenerated(final TopLevelClass topLevelClass, final IntrospectedTable introspectedTable) {
        return this.checkIsTableToGenerate(introspectedTable);
    }

    @Override
    public boolean clientGenerated(final Interface interfaze, final IntrospectedTable introspectedTable) {
        return this.checkIsTableToGenerate(introspectedTable);
    }

    @Override
    public boolean sqlMapGenerated(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
        return this.checkIsTableToGenerate(introspectedTable);
    }

}
