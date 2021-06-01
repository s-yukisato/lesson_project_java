package poker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PokerModel {
	 int games;
	 /** 現在のチップ数 初期値は500 */
	 int chips;
	 /** 山札 */
	 ArrayList<Integer> deckcards;
	 /** 手札 */
	 ArrayList<Integer> handcards;
	 /** 送信ボタンに表示する文字列("交換","次のゲーム"のどちらか */
	 String buttonLabel;
	 /** プレイヤーへのメッセージ */
	 String message;

	 public PokerModel() {
		 deckcards = new ArrayList<>();
		 handcards = new ArrayList<>();
	 }
	 /** 一連のゲームを開始する */
	 public void reset() {
		 games = 0;
		 chips = 500;
	 }
	 /** 次のゲームのためにカードを配りなおす */
	 public void nextgame() {
		// 52枚の山札を作る
		 deckcards.clear();
		 for (int i=1; i<=52; i++) {
			 deckcards.add(i);
		 }
//		 山札をシャッフルする
		 Collections.shuffle(deckcards);
		 // 山札の先頭から5枚抜いて手札にする
		 handcards.clear();
		 for (int i=0; i<5; i++) {
			 int n = deckcards.remove(0);
			 handcards.add(n);
		 }
//		 デバッグ用
//		 handcards.add(13);
//		 handcards.add(26);
//		 handcards.add(39);
//		 handcards.add(1);
//		 handcards.add(14);
		 // 手持ちのカードを小さい順に並べ替える
		 handcards = sortList(handcards);
		 // 残りの場札の枚数とカード番号をコンソールに表示する
		 System.out.printf("%d: ", deckcards.size());
		 for (int n: deckcards) {
			 System.out.printf("%d ", n);
		 }
		 message = "交換するカードをチェックしてください";
		 buttonLabel = "交換";
		 games++;
	 }
	 /** JSPから呼び出されるメソッド */
	 public int getGames() {
		 return games;
	 }
	 /** 現在のチップ数を返す */
	 public int getChips() {
		 return chips;
	 }
	 /** 5枚の手札のうち，i番目のカード番号を返す (i=0～4) */
	 public int getHandcardAt(int i) {
		 return handcards.get(i);
	 }
	 /** 送信ボタンのラベルを返す */
	 public String getButtonLabel() {
		 return buttonLabel;
	 }
	 /** プレイヤーへのメッセージを返す */
	 public String getMessage() {
		 return message;
	 }
	 /** 選択されたカードを入れ替える */
	 public void change(List<String> index) {
		 System.out.println("index=" + index);
		 for(int i=0; i<5; i++) {
			 if(index.contains("" + i)) {
				 int c = deckcards.remove(0);
				 handcards.set(i,  c);
			 }
		 }
		 handcards = sortList(handcards);
		 evaluate();
		 buttonLabel ="次のゲーム";
	 }

	 public void result(String rank, int point) {
		 message = "あなたのランクは  " + rank +  "\nPOINT:" + point ;
		 chips += point;
	 }

	 /** 役の判別を行い、チップを増減させる */
	 void evaluate() {
		 Map<String, Boolean> pokerHand = new HashMap<>() {{
			 put("One Pair", false);
			 put("Two Pair", false);
			 put("Three Of A Kind", false);
			 put("Straight", false);
			 put("Flush", false);
			 put("Full House", false);
			 put("Four Of A Kind", false);
			 // put("Straight Flush", false);  FlushとStraightを組み合わせる
			 put("Royal Flush", false);
		 }};
		 System.out.println(handcards);
		 // 手持ちカードのNumber List
		 List<Integer> handNumber = new ArrayList<Integer>(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0,0));
		 // 手持ちカードのSuit List
		 List<Integer> handSuit = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0)) ;
		 for(int i: handcards) {
			 int num = (i-1) % 13 + 1;
			 int type = (i-1) / 13;
			 handNumber.set(num-1, handNumber.get(num-1) + 1);
			 handSuit.set(type, handSuit.get(type) + 1);
		 }
		 System.out.println(handNumber);
		 // ペアの枚数で分岐処理
		 if(handNumber.contains(4)) { // four card
			 pokerHand.replace("Four Of A Kind", true);
		 } else if(handNumber.contains(3)) { // three card or full house
			 if(handNumber.contains(2)) pokerHand.replace("Full House", true);
			 else pokerHand.replace("Three Of A Kind", true);
		 } else if(handNumber.contains(2)) {  // ペアが存在する時、Two pair or One Pair
			 List<Integer> pairNum = handNumber.stream().filter(num -> num == 2).collect(Collectors.toList());
			 if(pairNum.size() == 2)  pokerHand.replace("Two Pair", true);
			 else pokerHand.replace("One Pair", true);
		 } else {  // ペアがない時、Royal Straight or Straight
			 int firstNum = handNumber.indexOf(1);
			 if(firstNum == 0 && !(handNumber.subList(9, 12).contains(0))) {
				 if(handSuit.contains(5)) pokerHand.replace("Royal Flush", true);
				 else pokerHand.replace("Straight", true);
			 } else if (!(handNumber.subList(firstNum, firstNum+4).contains(0))){
				 pokerHand.replace("Straight", true);
			 }
		 }
//		 手持ちのカードが全て同じsuitか判定
		 if(handSuit.contains(5)) pokerHand.replace("Flush", true);
//		 最終評価
		 if(pokerHand.get("Royal Flush")) {
			 result("ロイヤルストレートフラッシュ", 10000);
		 } else if (pokerHand.get("Straight") && pokerHand.get("Flush")) {
			 result("ストレートフラッシュ", 7500);
		 } else if (pokerHand.get("Four Of A Kind")) {
			 result("フォーカード", 5000);
		 } else if (pokerHand.get("Full House")) {
			 result("フルハウス", 3000);
		 } else if (pokerHand.get("Flush")) {
			 result("フラッシュ", 1500);
		 } else if (pokerHand.get("Straight")) {
			 result("ストレート", 1000);
		 } else if (pokerHand.get("Three Of A Kind")) {
			 result("スリーカード", 500);
		 } else if (pokerHand.get("Two Pair")) {
			 result("ツーペア", 300);
		 } else if (pokerHand.get("One Pair")) {
			 result("ワンペア", 100);
		 } else {
			 result("ノーハンド", -100);
		 }
		 if (chips == 0)  message += " ゲームオーバー";
	 }

    public static int calcMin(List<Integer> list) {
        int intMin = list.get(0);
        for (int i = 1; i < list.size(); i++ ) {
            intMin = intMin > list.get(i) ? list.get(i) : intMin;
         }
        return intMin;
    }

    public static ArrayList<Integer> sortList(ArrayList<Integer> list)  {
    	List<Integer> tmpList = new ArrayList<Integer>();
    	ArrayList<Integer> newList = new ArrayList<Integer>(list.size());
    	for(int i=0; i<5; i++) {
    		tmpList.add((list.get(i)-1)%13 + 1);
    		tmpList.add(list.get(i));
    	}
    	while(tmpList.size() != 0) {
    		int min = calcMin(tmpList);
    		int data = tmpList.remove(tmpList.indexOf(min) +1);
        	tmpList.remove(tmpList.indexOf(min));
        	newList.add(data);
    	}
        return newList;
    }
}
