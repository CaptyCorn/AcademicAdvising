import "bootstrap/dist/css/bootstrap.min.css";
import { Be_Vietnam_Pro } from "next/font/google";

const beVietnamPro = Be_Vietnam_Pro({
	variable: "--font-be-vietnam-pro",
	subsets: ["latin", "vietnamese"],
	weight: ["400", "500", "600", "700"],
});

export default function RootLayout({ children }: LayoutProps<"/">) {
	return (
		<html lang="en">
			<body className={beVietnamPro.className}>{children}</body>
		</html>
	);
}
