package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class DbUtils {

	private static final Logger log = LogManager.getLogger(DbUtils.class);

    public static boolean isUserRefNoPresentInDB(long ubirfnum) {

        String url = "jdbc:mysql://property.mariadb.prod.mbrsl.db:5133/property";
        String user = "qcmobotp_rousr";
        String password = "fkDKtv_3YmUjy2R@VzE@)s9#acqHrCQ";
       
        String query = "SELECT * FROM tpuot WHERE uotubirfnum = ? " +
                "ORDER BY CREATEDATE DESC LIMIT 1";
        
        try (Connection conn = DriverManager.getConnection(url, user, password);
                PreparedStatement ps = conn.prepareStatement(query)) {

               ps.setLong(1, ubirfnum);

               try (ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {

                // ✅ EXACT STYLE: print latest DB entry
                System.out.println("====== LATEST DB ENTRY ======");
                System.out.println("UOTRFNUM : " + rs.getLong("UOTRFNUM"));
                System.out.println("UOTUBIRFNUM : " + rs.getLong("UOTUBIRFNUM"));
                System.out.println("UOTSPMRFNUM : " + rs.getLong("UOTSPMRFNUM"));
                System.out.println("UOTCNDPAYSTATE : " + rs.getLong("UOTCNDPAYSTATE"));
                System.out.println("CREATEDATE     : " + rs.getTimestamp("CREATEDATE"));
                System.out.println("MODIDATE       : " + rs.getTimestamp("MODIDATE"));
                System.out.println("UOTSOURCE       : " + rs.getString("UOTSOURCE"));
                System.out.println("EXFIELD2        : " + rs.getString("EXFIELD2"));
                System.out.println("=============================");

                return true;
            }
               }

               return false;

           } catch (Exception e) {
        	   log.error("DB connection failed", e);
        	   return false;

    }
   /* public static void fetchTpustAndTpuseDataUsingUbi(long ubi) {

        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();

            // 🔹 Step 1: TPUST → get USTRFNUM + USTSPMNAME
            String tpustQuery = "SELECT USTRFNUM, USTSPMNAME FROM tpust " + "WHERE USTUBIRFNUM = " + ubi + " "
                    + "AND USTSPMRFNUM IN (356209, 356211) " + "AND USTISACTIVE = 'Y'";

            ResultSet tpustRs = stmt.executeQuery(tpustQuery);

            List<Long> ustrfNums = new ArrayList<>();

            System.out.println("====== TPUST DATA ======");
            while (tpustRs.next()) {
                long ustrfNum = tpustRs.getLong("USTRFNUM");
                String spmName = tpustRs.getString("USTSPMNAME");

                ustrfNums.add(ustrfNum);

                System.out.println("USTRFNUM : " + ustrfNum + " | USTSPMNAME : " + spmName);
            }
            System.out.println("========================");

            if (ustrfNums.isEmpty()) {
                System.out.println("❌ No TPUST data found for UBI: " + ubi);
                return;
            }

            // 🔹 Step 2: TPUSE → get USETOTALUNITS + USECNDSERVICE
            String inClause = ustrfNums.toString().replace("[", "(").replace("]", ")");

            String tpuseQuery = "SELECT USETOTALUNITS, USECNDSERVICE FROM tpuse " + "WHERE USEUSTRFNUM IN " + inClause;

            ResultSet tpuseRs = stmt.executeQuery(tpuseQuery);

            System.out.println("====== TPUSE DATA ======");
            while (tpuseRs.next()) {
                System.out.println("TOTAL UNITS : " + tpuseRs.getInt("USETOTALUNITS") + " | SECOND SERVICE : "
                        + tpuseRs.getString("USECNDSERVICE"));
            }
            System.out.println("========================");

        } catch (Exception e) {
            throw new RuntimeException("DB error while fetching TPUST/TPUSE data for UBI: " + ubi, e);
        }
    }*/

}
}