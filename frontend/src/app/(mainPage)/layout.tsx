import Header from "@/components/Header";
import { AuthProvider } from "../_context/AuthContext";

export default function MainPageLayout({ children }: LayoutProps<"/">) {
  return (
    <AuthProvider>
      <Header />
      {children}
    </AuthProvider>
  );
}
