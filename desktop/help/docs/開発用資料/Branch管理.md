## Branch リスト

| ブランチ名 | 概要                                                | 環境     | 命名規則                     |
| ---------- | --------------------------------------------------- | -------- | ---------------------------- |
| master     | 本番環境で運用するコード。                          | 本番環境 | ー                           |
| develop    | 開発環境で運用するコード。                          | 開発環境 | ー                           |
| hotfix     | master で発生したバグに緊急対応するためのブランチ。 | ー       | `hotfix/{issue番号}_{概要}`  |
| feature    | 実装中の Unstable なコード。                        | ー       | `feature/{issue番号}_{概要}` |

## イメージ図

![](https://i0.wp.com/lanziani.com/slides/gitflow/images/gitflow_1.png?zoom=2)
