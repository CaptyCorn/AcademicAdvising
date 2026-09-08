"use client";

import { useMemo } from "react";
import { Card } from "react-bootstrap";
import {
    Area,
    AreaChart,
    Bar,
    BarChart,
    CartesianGrid,
    Legend,
    ResponsiveContainer,
    Tooltip,
    XAxis,
    YAxis
} from "recharts";

interface IProps {
    dashboard: IDashboardStats,
    monthly: IMonthlyStats
}

interface IChartPoint {
    month: string,
    users?: number,
    posts?: number,
    books?: number,
    payments?: number
}

const mergeMonthlyStats = (seriesList: Array<{ data?: IMonthlyStatistic[], key: keyof Omit<IChartPoint, "month"> }>) => {
    const points = new Map<string, IChartPoint>();
    seriesList.forEach(({ data, key }) => {
        data?.forEach((item) => {
            const point = points.get(item.month) ?? { month: item.month };
            point[key] = item.total;
            points.set(item.month, point);
        });
    });

    return Array.from(points.values()).sort((left, right) => left.month.localeCompare(right.month));
};

const StatCard = ({ icon, label, value, color }: { icon: string, label: string, value: number, color: string }) => (
    <Card className="h-100 border-0 rounded-4 shadow-sm">
        <Card.Body className="d-flex align-items-center gap-3 p-4">
            <span className={`d-flex align-items-center justify-content-center rounded-3 bg-${color}-subtle text-${color}`} style={{ width: "44px", height: "44px" }}>
                <i className={`bi ${icon} fs-5`} aria-hidden="true" />
            </span>
            <div>
                <small className="d-block text-secondary">{label}</small>
                <strong className="fs-4 text-dark">{value.toLocaleString("vi-VN")}</strong>
            </div>
        </Card.Body>
    </Card>
);

const AdminDashboard = (props: IProps) => {
    const { dashboard, monthly } = props;
    const activityData = useMemo(() => mergeMonthlyStats([
        { data: monthly.usersByMonth, key: "users" },
        { data: monthly.postsByMonth, key: "posts" }
    ]), [monthly]);
    const bookData = useMemo(() => mergeMonthlyStats([
        { data: monthly.booksByMonth, key: "books" }
    ]), [monthly]);

    return (
        <main className="container-fluid p-4 p-lg-5">
            <div className="d-flex flex-wrap align-items-end justify-content-between gap-3 mb-4">
                <div>
                    <small className="text-uppercase fw-bold text-primary">Bảng điều khiển</small>
                    <h1 className="h3 mb-1 mt-2 fw-bold text-dark">Tổng quan hệ thống</h1>
                    <p className="mb-0 text-secondary">Theo dõi hoạt động chính của Academic Advising.</p>
                </div>
                <span className="badge rounded-pill bg-success-subtle text-success px-3 py-2">
                    <i className="bi bi-activity me-2" aria-hidden="true" />Hệ thống đang hoạt động
                </span>
            </div>

            <div className="row g-3 mb-4">
                <div className="col-12 col-sm-6 col-xl-3">
                    <StatCard icon="bi-people" label="Người dùng" value={dashboard.totalUsers} color="primary" />
                </div>
                <div className="col-12 col-sm-6 col-xl-3">
                    <StatCard icon="bi-file-post" label="Bài đăng" value={dashboard.totalPosts} color="success" />
                </div>
                <div className="col-12 col-sm-6 col-xl-3">
                    <StatCard icon="bi-chat-left-text" label="Bình luận" value={dashboard.totalComments} color="warning" />
                </div>
                <div className="col-12 col-sm-6 col-xl-3">
                    <StatCard icon="bi-book" label="Sách trao đổi" value={dashboard.totalBooks} color="info" />
                </div>
            </div>

            <div className="row g-4">
                <div className="col-12 col-xl-8">
                    <Card className="border-0 rounded-4 shadow-sm">
                        <Card.Body className="p-4">
                            <div className="d-flex align-items-center justify-content-between gap-3 mb-4">
                                <div>
                                    <h2 className="h5 mb-1 fw-bold">Hoạt động theo tháng</h2>
                                    <small className="text-secondary">Người dùng và bài đăng trong 12 tháng gần nhất</small>
                                </div>
                                <i className="bi bi-graph-up-arrow fs-4 text-primary" aria-hidden="true" />
                            </div>
                            {activityData.length > 0 ? (
                                <ResponsiveContainer width="100%" height={320}>
                                    <AreaChart data={activityData} margin={{ top: 8, right: 12, left: -20, bottom: 0 }}>
                                        <defs>
                                            <linearGradient id="usersFill" x1="0" y1="0" x2="0" y2="1">
                                                <stop offset="5%" stopColor="#0d6efd" stopOpacity={0.25} />
                                                <stop offset="95%" stopColor="#0d6efd" stopOpacity={0} />
                                            </linearGradient>
                                            <linearGradient id="postsFill" x1="0" y1="0" x2="0" y2="1">
                                                <stop offset="5%" stopColor="#198754" stopOpacity={0.22} />
                                                <stop offset="95%" stopColor="#198754" stopOpacity={0} />
                                            </linearGradient>
                                        </defs>
                                        <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#e9ecef" />
                                        <XAxis dataKey="month" tickLine={false} axisLine={false} />
                                        <YAxis allowDecimals={false} tickLine={false} axisLine={false} />
                                        <Tooltip />
                                        <Legend />
                                        <Area type="monotone" dataKey="users" name="Người dùng" stroke="#0d6efd" fill="url(#usersFill)" strokeWidth={2} />
                                        <Area type="monotone" dataKey="posts" name="Bài đăng" stroke="#198754" fill="url(#postsFill)" strokeWidth={2} />
                                    </AreaChart>
                                </ResponsiveContainer>
                            ) : (
                                <div className="py-5 text-center text-secondary">Chưa có dữ liệu theo tháng.</div>
                            )}
                        </Card.Body>
                    </Card>
                </div>

                <div className="col-12 col-xl-4">
                    <Card className="h-100 border-0 rounded-4 shadow-sm">
                        <Card.Body className="p-4">
                            <div className="mb-4">
                                <h2 className="h5 mb-1 fw-bold">Sách theo tháng</h2>
                                <small className="text-secondary">Số sách được đăng lên hệ thống</small>
                            </div>
                            {bookData.length > 0 ? (
                                <ResponsiveContainer width="100%" height={320}>
                                    <BarChart data={bookData} margin={{ top: 8, right: 8, left: -20, bottom: 0 }}>
                                        <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#e9ecef" />
                                        <XAxis dataKey="month" tickLine={false} axisLine={false} />
                                        <YAxis allowDecimals={false} tickLine={false} axisLine={false} />
                                        <Tooltip />
                                        <Bar dataKey="books" name="Sách" fill="#0dcaf0" radius={[5, 5, 0, 0]} />
                                    </BarChart>
                                </ResponsiveContainer>
                            ) : (
                                <div className="py-5 text-center text-secondary">Chưa có dữ liệu sách.</div>
                            )}
                        </Card.Body>
                    </Card>
                </div>
            </div>
        </main>
    );
};

export default AdminDashboard;
