import { useEffect, useRef } from "react";

import { useDebounce } from "./useDebounce";

export function useAutosave<TData, TReturn>({
  data,
  onSave,
  interval = 2000,
  saveOnUnmount = true,
}: {
  /** The controlled form value to be auto saved   */
  data: TData;
  /** Callback function to save your data */
  onSave: (data: TData) => Promise<TReturn> | TReturn | void;
  /** The number of milliseconds between save attempts. Defaults to 2000 */
  interval?: number;
  /** Set to false if you do not want the save function to fire on unmount */
  saveOnUnmount?: boolean;
}) {
  const valueOnCleanup = useRef(data);
  const initialRender = useRef(true);
  const handleSave = useRef(onSave);

  const debouncedValueToSave = useDebounce(data, interval);

  useEffect(() => {
    if (initialRender.current) {
      initialRender.current = false;
    } else {
      handleSave.current(debouncedValueToSave);
    }
  }, [debouncedValueToSave]);

  useEffect(() => {
    valueOnCleanup.current = data;
  }, [data]);

  useEffect(() => {
    handleSave.current = onSave;
  }, [onSave]);

  useEffect(
    () => () => {
      if (saveOnUnmount && data !== valueOnCleanup.current) {
        console.log("cleanup");
        handleSave.current(valueOnCleanup.current);
      }
    },
    [data, saveOnUnmount]
  );
}
