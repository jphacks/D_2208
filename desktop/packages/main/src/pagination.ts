import { keyboard, Key } from "@nut-tree/nut-js";

export const goNext = async () => {
  await keyboard.type(Key.Right);
  console.log("goNext");
};

export const goPrevious = async () => {
  await keyboard.type(Key.Left);
  console.log("goPrevious");
};
