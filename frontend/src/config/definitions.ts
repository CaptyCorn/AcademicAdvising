import z from "zod";

export const RegisterFormSchema = z.object({
    firstName: z
        .string()
        .min(2, { error: 'Từ 2 đến 15 kí tự' })
        .max(50, { error: "Ít hơn 50 kí tự" })
        .trim(),
    lastName: z
        .string()
        .min(2, { error: 'Từ 2 đến 15 kí tự' })
        .max(50, { error: "Ít hơn 50 kí tự" })
        .trim(),
    email: z
        .email({ error: "Email không được để trống" })
        .trim(),
    studentCode: z
        .string()
        .min(10, { error: "Ít nhất 10 kí tự" })
        .max(15, { error: "Tối đa 15 kí tự" }),
    username: z
        .string()
        .min(8, { error: 'Từ 8 đến 15 kí tự' })
        .max(50, { error: "Ít hơn 50 kí tự" })
        .trim(),
    password: z
        .string()
        .min(8, { error: 'Ít nhất 8 kí tự' })
        .regex(/[A-Z]/, { error: 'Chứa ít nhất 1 kí tự in hoa' })
        .regex(/[a-z]/, { error: 'Chứa ít nhất 1 kí tự in thường' })
        .regex(/[0-9]/, { error: 'Chứa ít nhất 1 kí tự số' })
        .regex(/[^a-zA-Z0-9]/, {
            error: 'Chứa ít nhất 1 kí tự đặc biệt',
        }),
    confirm: z
        .string({ error: "Không được để trống" })
        .trim()
}).refine(
    ({ password, confirm }) => password === confirm,
    {
        path: ["confirm"],
        error: "Mật khẩu xác nhận không khớp"
    }
)

export type RegisterValues = {
    firstName?: string
    lastName?: string
    email?: string
    studentCode?: string
    username?: string
    password?: string
    confirm?: string
}

export type FormState = {
        errors?: {
            firstName?: string[]
            lastName?: string[]
            email?: string[]
            studentCode?: string[]
            username?: string[]
            password?: string[]
            confirm?: string[]
        }
        values?: RegisterValues
        success?: boolean
        message?: string
    }