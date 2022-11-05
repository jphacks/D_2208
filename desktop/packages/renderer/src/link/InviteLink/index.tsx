import { Center, VStack } from "@chakra-ui/react";
import { toCanvas } from "qrcode";
import { useEffect, useRef, type FC } from "react";

import { Clipboard } from "./Clipboard";

const getQueryParam = (queryParamKey: string) => {
  const searchParams = new URLSearchParams(window.location.search);
  const param = searchParams.get(queryParamKey);
  if (param === null) {
    throw new Error(`query param key ${queryParamKey} is null`);
  }
  return param;
};

const roomId: string = getQueryParam("roomId");
const passcode: string = getQueryParam("passcode");
const inviteLinkOrigin = getQueryParam("origin");

const getInviteLink = () => {
  const url = new URL(inviteLinkOrigin);
  url.searchParams.set("roomId", roomId);
  url.searchParams.set("passcode", passcode);
  return url.toString();
};

const inviteLink: string = getInviteLink();

export const InviteLink: FC = () => {
  const canvasRef = useRef<HTMLCanvasElement | null>(null);

  useEffect(() => {
    const element = canvasRef.current;
    if (element === null) {
      throw new Error("canvas element is not found");
    }
    toCanvas(element, inviteLink, { width: 160 });
  }, [canvasRef]);

  return (
    <VStack p="4" w="full" h="full">
      <Center p="2" flexGrow={1}>
        <canvas
          ref={canvasRef}
          style={{
            width: "100%",
            height: "100%",
          }}
        />
      </Center>

      <VStack gap="2">
        <Clipboard title="URL" text={inviteLink} />
        <Clipboard title="ルームID" text={roomId} />
        <Clipboard title="passcode" text={passcode} />
      </VStack>
    </VStack>
  );
};
