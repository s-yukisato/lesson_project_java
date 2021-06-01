<%@ page language="java" contentType="text/html; charset=UTF-8"
 pageEncoding="UTF-8"%>
<%@ page import="poker.PokerModel" %>
<%
PokerModel model = (PokerModel)	request.getAttribute("model");
String label = model.getButtonLabel();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/poker.css">
<title>Poker</title>
</head>
<body>
	ポーカーゲーム
	<hr>
	ゲーム回数：<%= model.getGames() %>
	<br>
	<p>チップ：<%= model.getChips() %></p>
	<hr>
	<%= model.getMessage() %>
	<form action="/poker/PokerServlet" method="POST">
		<table>
			<tr align="center">
				<td>
					<input type="checkbox" name="change" value="0" id="check0" class="checkbox-parts">
					<label for="check0">
						<img src="cards/<%= model.getHandcardAt(0) %>.png" width="100" height="150">
					</label>
				</td>
				<td>
					<input type="checkbox" name="change" value="1" id="check1" class="checkbox-parts">
					<label for="check1" id="card1">
						<img src="cards/<%= model.getHandcardAt(1) %>.png" width="100" height="150">
					</label>
				</td>
				<td>
					<input type="checkbox" name="change" value="2" id="check2" class="checkbox-parts">
					<label for="check2" id="card2">
						<img src="cards/<%= model.getHandcardAt(2) %>.png" width="100" height="150">
					</label>
				</td>
				<td>
					<input type="checkbox" name="change" value="3" id="check3" class="checkbox-parts">
					<label for="check3" id="card3">
						<img src="cards/<%= model.getHandcardAt(3) %>.png" width="100" height="150">
					</label>
				</td>
				<td>
					<input type="checkbox" name="change" value="4" id="check4" class="checkbox-parts">
					<label for="check4" id="card4">
						<img src="cards/<%= model.getHandcardAt(4) %>.png" width="100" height="150">
					</label>
				</td>
			</tr>
		</table>
		<input type="submit" value="<%= label %>">
	</form>
	<hr>
	<a href="/s1932058/PokerServlet">リセット</a>
	<hr>
</body>
</html>