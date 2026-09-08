import { loadDashboardStats, loadMonthlyStats } from "@/actions/dashboard.action";
import AdminDashboard from "./_component/AdminDashboard";

const AdminPage = async () => {
    const [dashboard, monthly] = await Promise.all([
        loadDashboardStats(),
        loadMonthlyStats()
    ]);

    return <AdminDashboard dashboard={dashboard} monthly={monthly} />;
};

export default AdminPage;