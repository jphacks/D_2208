# ホスト PC 用 デスクトップアプリケーション

![version](https://img.shields.io/badge/version-1.1.0-blue.svg)

## 概要

ホスト PC 向けプレゼンテーションルーム管理アプリケーションです。

ルームの作成・終了、ルーム共有リンクの表示、参加者一覧の表示機能があります。

## 開発

### 環境構築

```sh
npm install
```

### 開発用サーバの起動

```sh
npm run dev
```

開発用バックエンドを使用する場合は、環境変数 `USE_DEV_BACKEND` を `true` に設定してください。

```sh
USE_DEV_BACKEND=true npm run dev
```

### コードチェック

```sh
npm run code-check
```

### ビルド方法

```sh
npm run build
```

### パッケージング

```sh
npm run pack
```

### 使用技術

- Electron@^8.25.0
- Vite@^3.1.8
- TypeScript@^4.6.8
