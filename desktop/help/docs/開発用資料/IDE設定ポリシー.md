JetBrains 系 IDE の設定方法を紹介します。

別のエディタ、IDE を利用する場合は設定方法を検索してください。

## IntelliJ IDEA

1. `git clone git@github.com:membership-console/formatter.git`
2. Eclipse Code Formatter プラグインをインストール
3. Use the Eclipse Code Formatter から`eclipse/formatter.xml`を選択する

## WebStorm

- `Languages & Frameworks > JavaScript > Prettier`から下記のように設定する
  - `Run for files`に`{**/*,*}.{js,ts,html,css,scss}`を指定
  - `On 'Reformat Code' action`にチェックを入れる
  - `On save`にチェックを入れる
- `Languages & Frameworks > JavaScript > Code Quality Tools > ESLint`から下記のように設定する
  - `Run for files`に`{**/*,*}.{js,ts,html}`を指定
  - `Run eslint --fix on save`にチェックを入れる
