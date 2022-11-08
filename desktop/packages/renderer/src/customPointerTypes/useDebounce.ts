import { useEffect, useState } from "react";

export function useDebounce<TData>(data: TData, interval: number) {
  const [liveData, setLiveData] = useState<TData>(data);

  useEffect(() => {
    const handler = setTimeout(() => {
      setLiveData(data);
    }, interval);
    return () => {
      clearTimeout(handler);
    };
  }, [data, interval]);

  return liveData;
}
