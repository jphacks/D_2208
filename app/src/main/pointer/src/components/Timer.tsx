import {
  Flex,
  NumberInput,
  NumberInputField,
  NumberInputStepper,
  NumberIncrementStepper,
  NumberDecrementStepper,
  Icon,
  VStack,
  Button,
  Modal,
  ModalOverlay,
  ModalContent,
  ModalHeader,
  ModalFooter,
  ModalBody,
  ModalCloseButton,
  useDisclosure,
  InputGroup,
  InputLeftAddon,
  InputRightAddon,
  useToast,
} from "@chakra-ui/react";
import { Cog6ToothIcon } from "@heroicons/react/24/solid";
import { Sink } from "graphql-ws";
import { FC, useEffect, useMemo, useState } from "react";
import { useForm } from "react-hook-form";

import { requestWs } from "@/api";
import { graphql } from "@/gql";
import { Timer as TimerData, TimerStatus } from "@/gql/graphql";
import { AuthData } from "@/types/AuthData";

type Props = {
  authData: AuthData;
};

type FormValues = {
  minutes: string;
  seconds: string;
};

export const Timer: FC<Props> = ({ authData }) => {
  const { isOpen, onOpen, onClose } = useDisclosure();
  const [timer, setTimer] = useState<TimerData | null>(null);
  const [noticeTime, setNoticeTime] = useState<string>("1");

  const toast = useToast();

  const completeNoticeTime = () => {
    if (noticeTime === "") {
      setNoticeTime("0");
    }
    onClose();
  };

  const { register, handleSubmit, setValue } = useForm<FormValues>();

  const mutationSink = useMemo<Sink>(
    () => ({
      // eslint-disable-next-line @typescript-eslint/no-empty-function
      next: () => {},
      // eslint-disable-next-line @typescript-eslint/no-empty-function
      complete: () => {},
      error: (error) => {
        if (!(error instanceof Error)) {
          throw error;
        }
        toast({
          title: "エラー",
          description: error.message,
          status: "error",
          duration: 5000,
          isClosable: true,
        });
      },
    }),
    [toast]
  );

  const onStartStop = (values: FormValues) => {
    console.log("SEND", values);
    switch (timer?.status) {
      case TimerStatus.Ready: {
        requestWs(
          {
            query: graphql(/* GraphQL */ `
              mutation StartTimer($inputTime: Int!, $accessToken: String!) {
                startTimer(inputTime: $inputTime, accessToken: $accessToken) {
                  status
                }
              }
            `),
            variables: {
              inputTime: Number(values.minutes) * 60 + Number(values.seconds),
              accessToken: authData.accessToken,
            },
          },
          mutationSink
        );
        return;
      }
      case TimerStatus.Paused: {
        requestWs(
          {
            query: graphql(/* GraphQL */ `
              mutation ResumeTime($accessToken: String!) {
                resumeTimer(accessToken: $accessToken) {
                  status
                }
              }
            `),
            variables: {
              accessToken: authData.accessToken,
            },
          },
          mutationSink
        );
        return;
      }
      case TimerStatus.Running: {
        requestWs(
          {
            query: graphql(/* GraphQL */ `
              mutation PauseTimer($accessToken: String!) {
                pauseTimer(accessToken: $accessToken) {
                  status
                }
              }
            `),
            variables: {
              accessToken: authData.accessToken,
            },
          },
          mutationSink
        );
        return;
      }
    }
  };

  const onReset = () => {
    requestWs(
      {
        query: graphql(/* GraphQL */ `
          mutation ResetTimer($accessToken: String!) {
            resetTimer(accessToken: $accessToken) {
              status
            }
          }
        `),
        variables: {
          accessToken: authData.accessToken,
        },
      },
      mutationSink
    );
  };

  useEffect(() => {
    requestWs(
      {
        query: graphql(/* GraphQL */ `
          subscription SubscribeTimer($roomId: ID!) {
            subscribeToTimer(roomId: $roomId) {
              status
              inputTime
              remainingTimeAtPaused
              finishAt
            }
          }
        `),
        variables: {
          roomId: authData.roomId,
        },
      },
      {
        next: ({ data, errors }) => {
          console.log({ data, errors });
          if (data) {
            setTimer(data.subscribeToTimer);
          }
          if (errors) {
            for (const error of errors) {
              toast({
                status: "error",
                description: error.message,
              });
            }
          }
        },
        error: mutationSink.error,
        complete: mutationSink.complete,
      }
    );
  }, [
    authData.roomId,
    mutationSink.complete,
    mutationSink.error,
    setTimer,
    setValue,
    toast,
  ]);

  useEffect(() => {
    if (timer === null) {
      return;
    }
    switch (timer.status) {
      case TimerStatus.Ready: {
        const { inputTime } = timer;

        const minutes = Math.floor(inputTime / 60);
        const seconds = inputTime % 60;
        setValue("minutes", minutes.toString());
        setValue("seconds", seconds.toString());
        return;
      }
      case TimerStatus.Paused: {
        const { remainingTimeAtPaused } = timer;

        if (remainingTimeAtPaused == null) {
          return;
        }

        const minutes = Math.floor(remainingTimeAtPaused / 60);
        const seconds = remainingTimeAtPaused % 60;
        setValue("minutes", minutes.toString());
        setValue("seconds", seconds.toString());

        return;
      }
      case TimerStatus.Running: {
        let requestId: number | null;

        const tick = () => {
          if (timer.status !== TimerStatus.Running) {
            return;
          }

          const timeStamp = new Date(timer.finishAt).valueOf() - Date.now();

          if (timeStamp <= 0) {
            setValue("minutes", "0");
            setValue("seconds", "0");
            setTimer((oldTimer) => {
              if (oldTimer === null) {
                return null;
              }
              return {
                status: TimerStatus.Ready,
                remainingTimeAtPaused: null,
                inputTime: oldTimer.inputTime,
                finishAt: oldTimer.finishAt,
              };
            });
            toast({
              title: "タイマーが終了しました",
              description: "お疲れ様でした",
              status: "success",
              duration: 9000,
              isClosable: true,
            });
            return;
          }

          const seconds = Math.floor((timeStamp / 1000) % 60);
          const minutes = Math.floor((timeStamp / 1000 / 60) % 60);
          setValue("minutes", minutes.toString());
          setValue("seconds", seconds.toString());

          requestId = requestAnimationFrame(tick);
        };
        requestId = requestAnimationFrame(tick);

        return () => {
          if (requestId) {
            cancelAnimationFrame(requestId);
          }
        };
      }
    }
  }, [timer, setValue, toast]);

  useEffect(() => {
    requestWs(
      {
        query: graphql(/* GraphQL */ `
          query GetTimer($roomId: ID!) {
            getTimer(roomId: $roomId) {
              status
              inputTime
              remainingTimeAtPaused
              finishAt
            }
          }
        `),
        variables: {
          roomId: authData.roomId,
        },
      },
      {
        ...mutationSink,
        next: ({ data, errors }) => {
          if (data) {
            setTimer(data.getTimer);
          }

          if (errors) {
            for (const error of errors) {
              toast({
                status: "error",
                description: error.message,
              });
            }
          }
        },
      }
    );
  }, [authData.roomId, mutationSink, toast]);

  return (
    <VStack gap={2}>
      <VStack gap={4} as="form" onSubmit={handleSubmit(onStartStop)}>
        <Flex justify="center" align="center">
          <NumberInput min={0} size="lg" flexGrow={1}>
            <NumberInputField
              textAlign="right"
              fontSize="5xl"
              height={24}
              {...register("minutes", {
                required: true,
                disabled: timer === null || timer.status !== TimerStatus.Ready,
              })}
            />
            <NumberInputStepper>
              <NumberIncrementStepper />
              <NumberDecrementStepper />
            </NumberInputStepper>
          </NumberInput>
          <Icon
            h={16}
            w={16}
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 24 24"
            fill="currentColor"
          >
            <path
              fillRule="evenodd"
              d="M10.5 6a1.5 1.5 0 113 0 1.5 1.5 0 01-3 0zm0 12a1.5 1.5 0 113 0 1.5 1.5 0 01-3 0z"
              clipRule="evenodd"
            />
          </Icon>
          <NumberInput min={0} max={59} size="lg" flexGrow={1}>
            <NumberInputField
              textAlign="right"
              fontSize="5xl"
              height={24}
              {...register("seconds", {
                required: true,
                disabled: timer === null || timer.status !== TimerStatus.Ready,
              })}
            />
            <NumberInputStepper>
              <NumberIncrementStepper />
              <NumberDecrementStepper />
            </NumberInputStepper>
          </NumberInput>
        </Flex>
        <Flex gap={8}>
          <Button width={24} type="submit" disabled={timer === null}>
            {timer?.status === TimerStatus.Running ? "ストップ" : "スタート"}
          </Button>
          <Button
            width={24}
            type="button"
            disabled={timer === null || timer.status === TimerStatus.Ready}
            onClick={onReset}
          >
            リセット
          </Button>
          <Button onClick={onOpen} disabled={timer === null}>
            <Icon as={Cog6ToothIcon} />
          </Button>
        </Flex>
      </VStack>
      <Modal isOpen={isOpen} onClose={completeNoticeTime}>
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>残り時間通知</ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            <InputGroup>
              <InputLeftAddon>残り</InputLeftAddon>
              <NumberInput
                min={0}
                value={noticeTime}
                onChange={(value) => setNoticeTime(value)}
              >
                <NumberInputField />
                <NumberInputStepper>
                  <NumberIncrementStepper />
                  <NumberDecrementStepper />
                </NumberInputStepper>
              </NumberInput>
              <InputRightAddon>分</InputRightAddon>
            </InputGroup>
          </ModalBody>

          <ModalFooter>
            <Button colorScheme="blue" mr={3} onClick={completeNoticeTime}>
              完了
            </Button>
          </ModalFooter>
        </ModalContent>
      </Modal>
    </VStack>
  );
};
