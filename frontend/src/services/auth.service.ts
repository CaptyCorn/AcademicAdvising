import { callAPI, endpoints } from "@/config/apis";

export const requestLogin = async (loginForm: {
    username: string,
    password: string
}) => {
    const res = await fetch(`${callAPI(endpoints["login"])}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(loginForm)
    });
    const responseInfo = await res.json();
    return responseInfo;
}

export const requestRegister = async (formData: FormData) => {
    const res = await fetch(`${callAPI(endpoints["register"])}`, {
        method: "POST",
        body: formData
    })
    if (res.status == 201) {
        const responseInfo = await res.json();
        return responseInfo;
    }
}

export const resquestProfile = async (token: string) => {
    const res = await fetch(`${callAPI(endpoints["profile"])}`, {
            headers: {
                "Content-Type": "application/json", 
                "Authorization": `Bearer ${token}`
            }
        });
    if (!res.ok) throw new Error("Token is valid");
    const data = await res.json();
    return data;
}