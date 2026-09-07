interface IImage {
    id: number,
    imageUrl: string,
    createdAt: Date
}

interface IBook {
    id: number, 
    name: string,
    price: number,
    condition: string,
    image?: IImage,
    createdAt: Date
}

interface IBookDetail {
    id: number, 
    name: string,
    price: number,
    description: string,
    condition: string,
    images: IImage[],
    subjects: ISubjectBook[]
}