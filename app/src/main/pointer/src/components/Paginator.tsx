import { IconButton, Icon, Flex, VStack, Heading } from "@chakra-ui/react";
import { ChevronLeftIcon, ChevronRightIcon } from "@heroicons/react/24/solid";
import { FC } from "react";

import { AuthData } from "@/types/AuthData";

type Props = {
  authData: AuthData;
};

export const Paginator: FC<Props> = ({ authData }) => {
  return (
    <Flex justify="center" gap={12} w="full">
      {[
        {
          icon: ChevronLeftIcon,
          label: "1ページ戻す",
          text: "前へ",
        },
        {
          icon: ChevronRightIcon,
          label: "1ページ進める",
          text: "次へ",
        },
      ].map(({ icon, label, text }, index) => (
        <VStack key={`${index}`} flexGrow="1">
          <Heading fontSize="xl">{text}</Heading>
          <IconButton
            aria-label={label}
            w="full"
            height={24}
            icon={<Icon width={14} height={14} as={icon} />}
          />
        </VStack>
      ))}
    </Flex>
  );
};
