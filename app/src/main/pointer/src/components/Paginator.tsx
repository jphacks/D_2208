import { IconButton, Icon, Flex, useToast } from "@chakra-ui/react";
import { ChevronLeftIcon, ChevronRightIcon } from "@heroicons/react/24/solid";
import { Sink } from "graphql-ws";
import { FC } from "react";

import { requestWs } from "@/api";
import { graphql } from "@/gql";
import { AuthData } from "@/types/AuthData";

type Props = {
  authData: AuthData;
};

// eslint-disable-next-line @typescript-eslint/no-empty-function
const noop = () => {};

export const Paginator: FC<Props> = ({ authData }) => {
  const toast = useToast();

  const emptySink: Sink = {
    next: noop,
    complete: noop,
    error(error) {
      if (!(error instanceof Error)) {
        throw error;
      }
      toast({
        status: "error",
        title: "エラーが発生しました",
        description: error.message,
      });
    },
  };

  const goNext = () => {
    requestWs(
      {
        query: graphql(/* GraphQL */ `
          mutation GoNextSlide($accessToken: String!) {
            goNextSlide(accessToken: $accessToken)
          }
        `),
        variables: {
          accessToken: authData.accessToken,
        },
      },
      emptySink
    );
  };

  const goPrevious = () => {
    requestWs(
      {
        query: graphql(/* GraphQL */ `
          mutation GoPreviousSlide($accessToken: String!) {
            goPreviousSlide(accessToken: $accessToken)
          }
        `),
        variables: {
          accessToken: authData.accessToken,
        },
      },
      emptySink
    );
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
