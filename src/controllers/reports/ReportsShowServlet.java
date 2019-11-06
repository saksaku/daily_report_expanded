package controllers.reports;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Comment;
import models.Employee;
import models.Report;
import models.validators.CommentValidator;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsShowServlet
 */
@WebServlet("/reports/show")
public class ReportsShowServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsShowServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));

        List<Comment> comments = em.createNamedQuery("getMyAllComments", Comment.class)
                .setParameter("report", r)
                .getResultList();

        em.close();

        request.setAttribute("report", r);
        request.setAttribute("_token", request.getSession().getId());
        request.setAttribute("comments", comments);

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/show.jsp");
        rd.forward(request, response);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        Comment c = new Comment();

        Report report = em.find(Report.class, Integer.parseInt(request.getParameter("report_id")));
        c.setReport((Report) report);

        c.setEmployee((Employee) request.getSession().getAttribute("login_employee"));

        c.setContent(request.getParameter("content"));

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        c.setCreated_at(currentTime);

        List<String> errors = CommentValidator.validate(c);
        if (errors.size() > 0) {

            em.close();

            request.setAttribute("_token", request.getSession().getId());
            request.setAttribute("comment", c);
            request.setAttribute("errors", errors);

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/show.jsp");
            rd.forward(request, response);
        } else {
            em.getTransaction().begin();
            em.persist(c);
            em.getTransaction().commit();
            em.close();
            request.getSession().setAttribute("flush", "投稿が完了しました。");

            response.sendRedirect(request.getContextPath() + "/reports/show?id=" + report.getId());
        }
    }
}