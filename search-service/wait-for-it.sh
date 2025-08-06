#!/usr/bin/env bash
# wait-for-it.sh

host="$1"
shift
cmd="$@"

until curl -s "http://$host" > /dev/null; do
  echo "Waiting for $host to be available..."
  sleep 5
done

exec $cmd
