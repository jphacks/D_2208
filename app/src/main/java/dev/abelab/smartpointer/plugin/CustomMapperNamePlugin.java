package dev.abelab.smartpointer.plugin;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;

/**
 * MyBatis Generatorで生成されるファイル名をカスタマイズするプラグイン
 */
public class CustomMapperNamePlugin extends PluginAdapter {

    @Override
    public boolean validate(final List<String> warnings) {
        return true;
    }

    @Override
    public void initialized(final IntrospectedTable introspectedTable) {
        super.initialized(introspectedTable);

        // 生成されるファイルをXxxMapperからXxxBaseMapperに変更する
        // 生成されたファイルに拡張内容を追記したくないので、XxxBaseMapperを継承したXxxMapperを手動で作成する
        final var javaMapperName = introspectedTable.getMyBatis3JavaMapperType();
        introspectedTable.setMyBatis3JavaMapperType(javaMapperName.replaceAll("Mapper$", "BaseMapper"));
        final var xmlMapperName = introspectedTable.getMyBatis3XmlMapperFileName();
        introspectedTable.setMyBatis3XmlMapperFileName(xmlMapperName.replaceAll("Mapper\\.xml", "BaseMapper.xml"));
    }

}
