import SideBar from "../_components/SideBar";
import { PostCreateProvider } from "../_context/PostCreateContext";
import styles from "./layout.module.css";

export default function MainPageLayout({ children }: LayoutProps<"/">) {
  return (
      <PostCreateProvider>
        <div className={`min-vh-100 bg-light ${styles.shell}`}>
          <SideBar />
          <main>{children}</main>
        </div>
      </PostCreateProvider>
  );
}
