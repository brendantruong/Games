export type Question = {
    points: number;
    question: string;
    answer: string;
    answered: boolean;
    imgSrc?: string;
    buzzers: string[];
};

export enum QuestionState {
    Open,
    Guessing,
    ShowAnswer
};

export type PlayerData = {
    name: string;
    socketId: string;
    score: number;
};