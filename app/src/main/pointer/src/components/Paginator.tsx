import { FC } from "react";
import { IconButton, Icon, Flex } from "@chakra-ui/react";
import { ChevronLeftIcon, ChevronRightIcon } from "@heroicons/react/24/solid";

export const Paginator: FC = () => {
  return (
    <Flex justify="center" gap={12}>
      {[
        { icon: ChevronLeftIcon, label: "1ページ戻す" },
        { icon: ChevronRightIcon, label: "1ページ進める" },
      ].map(({ icon, label }) => (
        <IconButton
          aria-label={label}
          width={24}
          height={24}
          icon={<Icon width={14} height={14} as={icon} />}
        />
      ))}
    </Flex>
  );
};
