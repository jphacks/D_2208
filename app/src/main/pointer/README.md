# リモコン用 アプリケーション

![version](https://img.shields.io/badge/version-1.1.0__SNAPSHOT-blue.svg)

## 概要

プレゼンテーション用レーザポインターを模したアプリケーションです。

ポインター表示、タイマーの設定、リモートでのスライドの切り替え機能を提供します。

## 開発

### 環境構築

```sh
yarn install
```

#### エディタの設定

以下を自動実行するように設定することを推奨します。

- フォーマッタ (Prettier)
- Linter (ESLint)
- テスト (Vitest)

[Visual Studio Code](https://code.visualstudio.com/) を使用する場合は、以下の拡張機能を使用することを推奨します。

- [ESLint](https://marketplace.visualstudio.com/items?itemName=dbaeumer.vscode-eslint)
- [Prettier - Code formatter](https://marketplace.visualstudio.com/items?itemName=esbenp.prettier-vscode)
- [Vitest](https://marketplace.visualstudio.com/items?itemName=ZixuanChen.vitest-explorer)

また、これらの拡張機能は、プロジェクトのフォルダ内で拡張機能を `@recommended` で検索すると表示されます。

変更する場合は `.vscode/extensions.json` ファイルを編集してください。

参考: [Workspace recommended extensions - Managing Extensions in Visual Studio Code](https://code.visualstudio.com/docs/editor/extension-marketplace#_workspace-recommended-extensions)

### 開発用サーバの起動

```sh
yarn run dev
```

### コードチェック

```sh
yarn run code-check
```

### ビルド方法

```sh
yarn build
```

### 使用技術

- React@^18.2.0
- Vite@^3.1.0
- TypeScript@^4.6.4
