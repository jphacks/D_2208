import { clipboard } from "electron";
import { toCanvas } from "qrcode";

const getQueryParam = ({
  queryParamKey,
  url,
}: {
  queryParamKey: string;
  url: string;
}) => {
  const urlSearchParams = new URLSearchParams(url);
  const param = urlSearchParams.get(queryParamKey);
  if (param === null) {
    throw new Error("roomId is null");
  }
  return param;
};

const getElementById = (id: string): HTMLElement => {
  const element = document.getElementById(id);
  if (element === null) {
    throw new Error(`Element not found: #${id}`);
  }
  console.log(element);
  return element;
};

const copyClipboard = (value: string) => {
  // TODO: ここでクリップボードにコピーする
  // http環境ではnavigator.clipboard.writeTextがエラーになるらしい。
  // electron の clipboard を使いたい
  // https://www.electronjs.org/docs/latest/api/clipboard
  clipboard.writeText(value);
};

const url: string = window.location.href;

getElementById("url").textContent = url;
getElementById("copy-url").addEventListener("click", () => copyClipboard(url));

const roomId: string = getQueryParam({ queryParamKey: "roomId", url });
getElementById("roomId").textContent = roomId;
getElementById("copy-roomId").addEventListener("click", () =>
  copyClipboard(roomId)
);

const passcode: string = getQueryParam({ queryParamKey: "passcode", url });
getElementById("passcode").textContent = passcode;
getElementById("copy-passcode").addEventListener("click", () =>
  copyClipboard(passcode)
);

// QR コードを表示
toCanvas(getElementById("qrcode"), url);
