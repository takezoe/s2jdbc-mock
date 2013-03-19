s2jdbc-mock
=================

S2JDBCを使ったサービスやDAOのユニットテストを支援するライブラリです。
実際にDBに接続するのではなく、実行されたSQLが期待通りのものかどうかをチェックするための手段を提供します。

s2jdbc-mockを使用するにはpom.xmlに以下の依存関係を追加します。

```xml
<repositories>
  <repository>
    <id>amateras</id>
    <name>Project Amateras Maven2 Repository</name>
    <url>http://amateras.sourceforge.jp/mvn/</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>org.seasar.s2jdbcmock</groupId>
    <artifactId>s2jdbc-mock</artifactId>
    <version>1.0.0</version>
  </dependency>
</dependencies>
```

設定ではユニットテスト用のs2jdbc.diconでJdbcManagerを定義している部分をorg.seasar.s2jdbcmock.MockJdbcManagerに変更するだけです。

テストケースではorg.seasar.s2jdbcmock.S2JdbcUnitのメンバをstaticインポートしてSQLの検証を行うことができます。
以下はS2JUnit4を使用した場合のテストケースの例です。

```java
package org.seasar.s2jdbcmock.service;

import static junit.framework.Assert.*;
import static org.seasar.s2jdbcmock.S2JdbcUnit.*;

...

@RunWith(Seasar2.class)
public class EmployeeServiceTest {

  protected EmployeeService sampleService;

  @Before
  public void init(){
    initS2JdbcUnit();
  }

  @Test
  public void testGetEmployeeTx(){
    // サービスのメソッドを実行
    sampleService.getEmployee(1);
    // 正規表現でSQLを検証
    verifySqlByRegExp(0, "SELECT .* FROM EMPLOYEE .* WHERE .*EMP_ID = \\?", 1);
  }

}
```

上記の例では正規表現で検証していますが、SQLは空白や改行、大文字小文字、コメントなどを適度に正規化して比較しているので、
厳密に文字列として完全一致するように記述しなくても大丈夫です。

サービスクラスのメソッドによってはJdbcManagerの戻り値によって処理が分岐するようなケースもあるでしょう。
そのような場合はS2JdbcUnit#addResult()メソッドでJdbcManagerが返却する値を設定することができます。
MockJdbcManagerはSQLを実行するためにaddResult()された値を順番に返却します
（INSERTやUPDATE時の更新件数も戻り値なのでaddResult()で設定しておくことができます）。

```java
@Test
public void testGetEmployeeTx(){
  // JdbcManagerの返却値を設定
  Employee result = new Employee();
  ...
  addResult(result);

  // サービスのメソッドを実行（上でセットしたエンティティが返却される）
  Employee employee = sampleService.getEmployeeList();
  ...
}
```

リリースノート
--------
### 1.0.0 - 20 Mar 2013

* AmaterasのMavenリポジトリにデプロイして利用できるようにしました。
