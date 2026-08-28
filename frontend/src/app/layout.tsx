import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap-icons/font/bootstrap-icons.css";
import { Be_Vietnam_Pro } from "next/font/google";
import { AuthProvider } from "./_context/AuthContext";
import { Bounce, ToastContainer } from "react-toastify";

const beVietnamPro = Be_Vietnam_Pro({
	variable: "--font-be-vietnam-pro",
	subsets: ["latin", "vietnamese"],
	weight: ["400", "500", "600", "700"],
});

export default function RootLayout({ children }: LayoutProps<"/">) {
	return (
		<html lang="en">
			<body className={beVietnamPro.className}>
				<AuthProvider>
					{children}
				</AuthProvider>
				<ToastContainer 
					position="bottom-right"
					autoClose={5000}
					hideProgressBar={false}
					newestOnTop={false}
					closeOnClick={false}
					rtl={false}
					pauseOnFocusLoss
					draggable
					pauseOnHover
					theme="light"
					transition={Bounce}
				/>
			</body>
		</html>
	);
}
