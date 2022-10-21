import { IconButton, Icon, Flex, VStack, Heading } from "@chakra-ui/react";
import { ChevronLeftIcon, ChevronRightIcon } from "@heroicons/react/24/solid";
import { FC } from "react";

import { client } from "@/stomp";
import { AuthData } from "@/types/AuthData";

type Props = {
  authData: AuthData;
};

export const Paginator: FC<Props> = ({ authData }) => {
  const goNext = () =>
    client.publish({
      destination: `/app/rooms/${authData.roomId}/slides/next`,
      body: JSON.stringify({ room_id: authData.roomId }),
    });

  const goPrevious = () =>
    client.publish({
      destination: `/app/rooms/${authData.roomId}/slides/previous`,
      body: JSON.stringify({ room_id: authData.roomId }),
    });

  return (
    <Flex justify="center" gap={12} w="full">
      {[
        {
          icon: ChevronLeftIcon,
          label: "1ページ戻す",
          text: "前へ",
          onClick: goPrevious,
        },
        {
          icon: ChevronRightIcon,
          label: "1ページ進める",
          text: "次へ",
          onClick: goNext,
        },
      ].map(({ icon, label, text, onClick }, index) => (
        <VStack key={`${index}`} flexGrow="1">
          <Heading fontSize="xl">{text}</Heading>
          <IconButton
            aria-label={label}
            w="full"
            height={24}
            icon={<Icon width={14} height={14} as={icon} onClick={onClick} />}
          />
        </VStack>
      ))}
    </Flex>
  );
};
