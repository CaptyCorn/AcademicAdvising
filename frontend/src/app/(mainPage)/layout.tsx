import SideBar from "../_components/SideBar";
import styles from "./layout.module.css";

export default function MainPageLayout({ children }: LayoutProps<"/">) {
  return (
      <div className={`min-vh-100 bg-light ${styles.shell}`}>
        <SideBar />
        <main>{children}</main>
      </div>
  );
}
