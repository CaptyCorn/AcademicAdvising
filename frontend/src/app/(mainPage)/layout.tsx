import SideBar from "../_components/SideBar";

export default function MainPageLayout({ children }: LayoutProps<"/">) {
  return (
      <div className="d-flex min-vh-100 bg-light">
        <SideBar />
        <main className="flex-grow-1">{children}</main>
      </div>
  );
}
