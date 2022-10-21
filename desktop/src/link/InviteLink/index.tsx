import { Center, Container, VStack } from "@chakra-ui/react";
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

const getInviteLink = (roomId: string, passcode: string) => {
  const inviteLinkOrigin = "https://smartpointer.abelab.dev/";
  const url = new URL(inviteLinkOrigin);
  url.searchParams.append("roomId", roomId);
  url.searchParams.set("passcode", passcode);
  return url.toString();
};

const inviteLink: string = getInviteLink(roomId, passcode);

export const InviteLink: FC = () => {
  const canvasRef = useRef<HTMLCanvasElement | null>(null);

  useEffect(() => {
    const element = canvasRef.current;
    if (element === null) {
      throw new Error("canvas element is not found");
    }
    // TODO: 環境変数にする
    toCanvas(element, inviteLink);
  }, [canvasRef]);

  return (
    <Container>
      <Center p="4">
        <canvas id="canvas" ref={canvasRef} />
      </Center>

      <VStack gap="2">
        <Clipboard title="URL" text={inviteLink} />
        <Clipboard title="ルームID" text={roomId} />
        <Clipboard title="passcode" text={passcode} />
      </VStack>
    </Container>
  );
};
