<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:choose>
            <c:when test="${report != null}">
                <h2>日報　詳細ページ</h2>

                <table>
                    <tbody>
                        <tr>
                            <th>氏名</th>
                            <td><c:out value="${report.employee.name}" /></td>
                        </tr>
                        <tr>
                            <th>日付</th>
                            <td><fmt:formatDate value='${report.report_date}' pattern='yyyy-MM-dd' /></td>
                        </tr>
                        <tr>
                            <th>内容</th>
                            <td>
                                <pre><c:out value="${report.content}" /></pre>
                            </td>
                        </tr>
                        <tr>
                            <th>登録日時</th>
                            <td>
                                <fmt:formatDate value="${report.created_at}" pattern="yyyy-MM-dd HH:mm:ss" />
                            </td>
                        </tr>
                        <tr>
                            <th>更新日時</th>
                            <td>
                                <fmt:formatDate value="${report.updated_at}" pattern="yyyy-MM-dd HH:mm:ss" />
                            </td>
                        </tr>
                    </tbody>
                </table>

                <c:if test="${sessionScope.login_employee.id == report.employee.id}">
                    <p><a href="<c:url value='/reports/edit?id=${report.id}' />">この日報を編集する</a></p>
                </c:if>
            </c:when>
            <c:otherwise>
                <h2>お探しのデータは見つかりませんでした。</h2>
            </c:otherwise>
        </c:choose>
        <p><a href="<c:url value='/reports/index' />">一覧に戻る</a></p>
        <br /><br />

        <h2>コメントを投稿する</h2>
<form method="POST" action="<c:url value='/reports/show' />">
<c:if test="${errors != null}">
    <div id="flush_error">
        入力内容にエラーがあります。<br />
        <c:forEach var="error" items="${errors}">
            ・<c:out value="${error}" /><br />
        </c:forEach>
    </div>
</c:if>

<label for="name">氏名</label><br />
<c:out value="${sessionScope.login_employee.name}" />
<br /><br />

<label for="content">内容</label><br />
<textarea name="content" rows="10" cols="50">${comment.content}</textarea>
<br /><br />

<input type="hidden" name="report_id" value="${report.id}" />

<input type="hidden" name="_token" value="${_token}" />

<button type="submit">投稿</button>
<br /><br /></form>

        <h2>コメント一覧</h2>
        <div style="overflow:scroll;">
            <c:forEach var="comment" items="${comments}">
                        <fmt:formatDate value="${comment.created_at}" pattern="yyyy-MM-dd HH:mm:ss" />
                        <br />
                        <c:out value="${comment.employee.name}" />
                        <br />
                        <c:out value="${comment.content}" />
                        <br /><br />
            </c:forEach>
        </div>
        <br /><br />
    </c:param>
</c:import>