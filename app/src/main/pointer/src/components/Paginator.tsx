import { IconButton, Icon, Flex } from "@chakra-ui/react";
import { ChevronLeftIcon, ChevronRightIcon } from "@heroicons/react/24/solid";
import { FC } from "react";

export const Paginator: FC = () => {
  return (
    <Flex justify="center" gap={12}>
      {[
        { icon: ChevronLeftIcon, label: "1ページ戻す" },
        { icon: ChevronRightIcon, label: "1ページ進める" },
      ].map(({ icon, label }, index) => (
        <IconButton
          key={`${index}`}
          aria-label={label}
          flexGrow={1}
          height={24}
          icon={<Icon width={14} height={14} as={icon} />}
        />
      ))}
    </Flex>
  );
};
