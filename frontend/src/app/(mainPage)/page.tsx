import { callAPI, endpoints } from "@/config/apis";
import { cookies } from "next/headers";

const HomePage = async () => {
  const cookieStore = await cookies();
  const token = cookieStore.get('token')?.value
  const res = await fetch(`${callAPI(endpoints['posts'])}`, {
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    }
  })
  
  const responseInfo = await res.json();
  console.log(responseInfo.data)

  return(
    <div>heelo</div>
  );
}

export default HomePage;
