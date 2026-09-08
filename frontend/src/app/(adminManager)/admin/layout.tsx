import SideBarAdmin from "./_component/SideBarAdmin";

export default function MainPageLayout({ children }: LayoutProps<"/">) {
    return (
        <div className="d-flex min-vh-100 bg-light">
            <SideBarAdmin />
            <main className="flex-grow-1">{children}</main>
        </div>
    );
}
