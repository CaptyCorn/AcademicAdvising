interface IDashboardStats {
    totalUsers: number,
    totalPosts: number,
    totalComments: number,
    totalBooks: number,
    pendingReports?: number
}

interface IMonthlyStatistic {
    month: string,
    total: number
}

interface IMonthlyStats {
    usersByMonth?: IMonthlyStatistic[],
    postsByMonth?: IMonthlyStatistic[],
    booksByMonth?: IMonthlyStatistic[],
    paymentsByMonth?: IMonthlyStatistic[]
}
