import argparse, sys as system
import os

parser = argparse.ArgumentParser(description='Client perf program')
parser.add_argument('--clients', type=int, help='The number of clients')
parser.add_argument("--serverjar", type=int, help="Location of server jar")
parser.add_argument("--perfjar", type=int, help="Location of performance jar")
parser.add_argument("--port", type=int, help="Server port")
parser.add_argument("--umsg", type=int, help="Total number of unicast messages to send")
parser.add_argument("--bmsg", type=int, help="Total number of broadcast messages to send")
parser.add_argument("--lmsg", type=int, help="Total number of list messages to send")
args = parser.parse_args()

def validate_arg(argument):
    if argument == None or argument == "":
        print argument + " can not be empty"
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

print "Trying to start the server on port: " + server_port
exit_code = os.system("java -jar " + server_jar_name + " -p " + server_port)
print exit_code