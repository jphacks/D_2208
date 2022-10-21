import { FC, useState } from "react";

export const App: FC = () => {
  const showQRCode = useState(false);

  return (
    <div>{showQRCode ? <div>QRコード</div> : <div>QRコードじゃない</div>}</div>
  );
};
