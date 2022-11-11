import { IconButton, Icon, Flex } from "@chakra-ui/react";
import { ChevronLeftIcon, ChevronRightIcon } from "@heroicons/react/24/solid";
import { FC } from "react";

// import { stompClient } from "@/stomp";
import { AuthData } from "@/types/AuthData";

type Props = {
  authData: AuthData;
};

export const Paginator: FC<Props> = ({ authData }) => {
  const goNext = () => {
    // TODO: GraphQL 移行
    // stompClient.publish({
    //   destination: `/app/rooms/${authData.roomId}/slides/next`,
    //   body: JSON.stringify({ room_id: authData.roomId }),
    // });
  };

  const goPrevious = () => {
    // TODO: GraphQL 移行
    // stompClient.publish({
    //   destination: `/app/rooms/${authData.roomId}/slides/previous`,
    //   body: JSON.stringify({ room_id: authData.roomId }),
    // });
  };

  return (
    <Flex
      justify="center"
      gap={12}
      w="full"
      flexGrow={1}
      alignItems="stretch"
      sx={{
        "& button": {
          height: "100%",
          flexGrow: 1,
        },
      }}
    >
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
      ].map(({ icon, label, onClick }, index) => (
        <IconButton
          key={`${index}`}
          aria-label={label}
          icon={<Icon width={14} height={14} as={icon} onClick={onClick} />}
        />
      ))}
    </Flex>
  );
};
