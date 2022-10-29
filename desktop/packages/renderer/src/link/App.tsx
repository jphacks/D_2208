import { FC, useState } from "react";

import { Confirmation } from "./Confirmation";
import { InviteLink } from "./InviteLink";

export const App: FC = () => {
  const [showInviteLink, setShowInviteLink] = useState(false);

  return (
    <>
      {showInviteLink ? (
        <InviteLink />
      ) : (
        <Confirmation onConfirm={() => setShowInviteLink(true)} />
      )}
    </>
  );
};
