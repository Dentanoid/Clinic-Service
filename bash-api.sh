echo "API Childprocess launched!"
cd Clinic-Service/src/main/java/generalPackage
node GoogleAPI

( sleep 20 ) & pid=$!
( sleep 5 && kill -HUP $pid ) 2>/dev/null & watcher=$!
if wait $pid 2>/dev/null; then
    echo "your_command finished"
    pkill -HUP -P $watcher
    wait $watcher
else
    echo "your_command interrupted"
fi