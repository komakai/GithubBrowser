## 設定
personal access tokenを以下のように設定してください

local.propertiesファイルにgithub.patのプロパティを指定する
```
github.pat={token}
```

## アピールしたいポイント
### データロード
Pagingを利用して、一致するユーザーが沢山いても、一括で、データをロードせず、スクロールしながら、背景で、追加の結果をロードさせます。

### 依存性の注入（Dependency Injection）
２つの主なコンポーネントがあります
* GithubService - ユーザーとリポジトリのデータを取得するコンポーネントです。メインアプリでretrofit/OKHTTPを利用して、テストする時、Mockサービスが付け替えられます。
* ImageDownloader - アイコン画像をダウンロード及び表示するコンポーネントです。メインアプリでGlideを利用して、テストする時、Mockダウンローダーが付け替えられます。
