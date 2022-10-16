# Smart Pointer

![CI](https://github.com/jphacks/D_2208/workflows/CI/badge.svg)
![deploy](https://github.com/jphacks/D_2208/workflows/deploy/badge.svg)
![version](https://img.shields.io/badge/version-1.0.0__SNAPSHOT-blue.svg)

[![IMAGE ALT TEXT HERE](https://jphacks.com/wp-content/uploads/2022/08/JPHACKS2022_ogp.jpg)](https://www.youtube.com/watch?v=LUPQFB4QyVo)

## 製品概要

あなたのスマホがリモコンに！？

Smart Pointerは手元のスマホをリモコン化する、クラウドベースな共同プレゼンテーション支援ツールです。

> **Note**
>
> 製品概要の詳細は[PRD](https://github.com/jphacks/D_2208/wiki/PRD（プロダクト要求仕様書）)を参照してください

### 背景(製品開発のきっかけ、課題等）

複数人でプレゼンテーションを行う場合、スライドの切り替え担当者とスピーカーが一致しない場合があります。

スピーカーが逐一「次のスライドお願いします」と依頼するのは面倒なので、手元のスマホをリモコン化し、リモートにあるホストPCのスライドを操作できるようにします。

### 製品説明（具体的な製品の説明）

### 特長

#### 1. 特長1

#### 2. 特長2

#### 3. 特長3

### 解決出来ること

> TODO: 後ほど記載する

### 今後の展望

> TODO: 後ほど記載する

### 注力したこと（こだわり等）

> TODO: 後ほど記載する

*
*

### 用語集

* リモコン
    * プレゼンテーション用のレーザーポインター的な概念
    * 自身のスマートフォンをリモコン化し、リモートにあるPCのスライドを操作できる！
* ルーム
    * 共同プレゼンターが参加し、スライドをリモコン操作するためのワークスペース
* ホストPC
    * スライド投影するPC
    * [デスクトップアプリケーション](./desktop)を実行する
* 参加者
    * ホストPCが作成したルームに
    * [リモコン用Webアプリ](./app/src/main/pointer)を実行する
* ポインタ渡し
    * プレゼンター権限を譲渡する行為

## 開発技術

### 活用した技術

#### フレームワーク・ライブラリ・モジュール

* デスクトップアプリケーション
    * Node.js 16
    * Electron
    * TypeScript
* リモコン用Webアプリ
    * Node.js 16
    * React
    * Vite
    * TypeScript
* APIサーバ
    * Spring Boot 2.7
    * Java OpenJDK 11
    * Spock
    * MySQL 8.0
    * AsyncAPI
* インフラ
    * Google App Engine
    * Google Cloud SQL
    * Google Cloud Storage
* CI/CD
    * GitHub Actions
    * Jenkins

### 独自技術

#### ハッカソンで開発した独自機能・技術

> TODO: 後ほど記載する

* 独自で開発したものの内容をこちらに記載してください
* 特に力を入れた部分をファイルリンク、またはcommit_idを記載してください。