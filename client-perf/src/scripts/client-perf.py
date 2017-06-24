import argparse, sys as system
import time
import uuid
from subprocess import Popen, call


parser = argparse.ArgumentParser(description='Client perf program')
parser.add_argument('--clients', type=int, help='The number of clients')
parser.add_argument("--serverjar", type=str, help="Location of server jar")
parser.add_argument("--perfjar", type=str, help="Location of performance jar")
parser.add_argument("--port", type=int, help="Server port")
parser.add_argument("--umsg", type=int, help="Total number of unicast messages to send")
parser.add_argument("--bmsg", type=int, help="Total number of broadcast messages to send")
parser.add_argument("--lmsg", type=int, help="Total number of list messages to send")
args = parser.parse_args()

def validate_arg(argument):
    if argument == None or argument == "":
        print("Empty argument. Try again")
        # TODO show the help menu
        system.exit(0)

# the total number of clients
num_of_clients = args.clients
# the server jar name
server_jar_name = args.serverjar
# the perf jar name
perf_jar_name = args.perfjar
# the server port
server_port = args.port
# the total number of unicast messages to send
num_of_unicast_msg = args.umsg
# the total number of broadcast messages to send
num_of_broadcast_msg = args.bmsg
# the total number of list message to send
num_of_list_msg = args.lmsg

args = [num_of_clients, server_jar_name, perf_jar_name, server_port,
    num_of_unicast_msg, num_of_broadcast_msg, num_of_list_msg]

# validate the arguments - all of them are required
for arg in args:
    validate_arg(arg)

print("Trying to start the server on port: " + str(server_port))
server_args = [server_jar_name, "-p", str(server_port)]
JAR_COMMAND = ['java', '-jar']
server = Popen(JAR_COMMAND + server_args)
time.sleep(5) # wait 5 seconds in order server to start
if server.poll():
    print("Server does not start in the timely manner.")
    system.exit(1)
report_dir = "report_" + uuid.uuid4().hex
call(["mkdir", report_dir]) # create a report directory
clients = []
for i in range(0, int(num_of_clients)):
    client_arg = [perf_jar_name, "-p", str(server_port), "-cname", str(i), "-sfile", report_dir, 
    "-bmsg", str(num_of_broadcast_msg), "-umsg", str(num_of_unicast_msg), "-lmsg", str(num_of_list_msg)]
    clients.append(Popen(JAR_COMMAND + client_arg))
    print("Start client " + str(i))
# python client.py --serverjar simple-chat-server.jar --perfjar simple-chat-client-perf.jar --clients 10 --port 5556 --umsg 10 --bmsg 10 --lmsg 10
