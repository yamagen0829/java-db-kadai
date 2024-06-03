package kadai_007;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Posts_Chapter07 {

	public static void main(String[] args) {
		   
		Connection con = null;
		PreparedStatement statement = null;
		
		
		//ユーザーリスト
		String[][]userList = {
				{"1003", "2023-02-08","昨日の夜は徹夜でした‥","13"},
				{"1002", "2023-02-08", "お疲れ様です！", "12"},
				{"1003", "2023-02-09", "今日も頑張ります！", "18"},
				{"1001", "2023-02-09", "無理は禁物ですよ！" , "17"},
				{"1002", "2023-02-10", "明日から連休ですね！", "20"}
         };
        
		try {
			//データベースに接続
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost/challenge_java",
					"root",
					"Ilovemusic1"
			);
			
			System.out.println("データベース接続成功:" + con);
			
			//SQLクエリを準備
			String sql ="INSERT INTO posts (user_id, posted_at,post_content,likes) VALUES (?, ?, ?, ?);";
			statement = con.prepareStatement(sql);
						
			//リストの1行目から順番に読み込む
			int rowCnt = 0;
			for(int i = 0; i < userList.length; i++) {
				//SQLクエリの「？」部分をリストのデータに置き換え
				statement.setString(1, userList[i][0]);//id
				statement.setString(2, userList[i][1]);//日時
				statement.setString(3, userList[i][2]);//内容
				statement.setString(4, userList[i][3]);//いいね数
	
				//SQLクエリを実行（DBMSに送信）
				rowCnt += statement.executeUpdate();
				}
			System.out.println("レコード追加を実行します");
			System.out.println( rowCnt + "件のレコードが追加されました");
			    
			try (Statement selectstatement = con.createStatement()){
			    //SQLクエリを準備
			    String sqlSelect ="SELECT * FROM posts WHERE user_id = 1002;";
			    System.out.println("ユーザーIDが1002のレコードを検索しました");
                
			    //SQLクエリを実行
			    ResultSet result = selectstatement.executeQuery(sqlSelect);
			    
			    int count = 0;
			    //SQLクエリの実行結果を抽出
			    while(result.next()) {
			    	count++;
			    	Date postedAt = result.getDate("posted_at");
			    	String postContent = result.getString("post_content");
			    	int likes = result.getInt("likes");
			    	
			    	System.out.println(result.getRow() + "件目:投稿日時=" + postedAt + "/投稿内容=" + postContent + "/いいね数＝" + likes);
			    	
			    	if (count >= 2) {
			    		break;
			    	}
			    }
			} catch (SQLException e) {
				e.printStackTrace();
				}  
		} catch(SQLException e) {
			System.out.println("エラー発生:" + e.getMessage());
		} finally {
			//使用したオブジェクトを解放
			if( statement != null ) {
				try { statement.close(); } catch(SQLException ignore) {}
			}
			if( con != null ) {
				try { con.close(); } catch(SQLException ignore) {}
			}
		}
	}

}