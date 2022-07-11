import React from 'react'
import './App.css'

interface PluginProps {
  name: string
  onClick: (e: React.MouseEvent<HTMLButtonElement>) => Promise<void>
}

interface DashBoard {
  state: string
  pluginNames: string[]
}

class PluginButton extends React.Component<PluginProps, {}> {
  render (): JSX.Element {
    // The plugin to be chosen when initializing the framework.
    return (
      <button className='button' onClick={this.props.onClick}>
        {this.props.name}
      </button>
    )
  }
}

class App extends React.Component<{}, DashBoard> {
  constructor (props: {}) {
    super(props)
    this.state = {
      state: 'busy',
      pluginNames: []
    }
    this.handleNewFramework = this.handleNewFramework.bind(this)
  }

  componentDidMount (): void {
    // fetch json after refreshing.
    void this.initDashBoard()
  }

  stateUpdate (json: any): void {
    // Update the state after fetching the JSON.
    this.setState({
      state: json.state,
      pluginNames: json.pluginNames
    })
  }

  async initDashBoard (): Promise<void> {
    const response = await fetch('fetch')
    const json = await response.json()
    this.stateUpdate(json)
  }

  async handleClick (index: number): Promise<void> {
    // Handle the event that the user clicks on a plugin.
    if (this.state.state === 'chooseVisualizationPlugin') {
      this.setState({
        state: 'busy'
      })
    }
    const response = await fetch(`click?index=${index}`)
    const json = await response.json()
    this.stateUpdate(json)
  }

  async handleNewFramework (): Promise<void> {
    // Handle the event that the user clicks on the start again button.
    const response = await fetch('new')
    const json = await response.json()
    this.stateUpdate(json)
  }

  renderPlugin (): JSX.Element[] {
    // Render all the buttons for choosing plugins.
    const plugins: JSX.Element[] = []
    for (let i = 0; i < this.state.pluginNames.length; i++) {
      plugins.push(
        <div key={i}>
          <PluginButton
            name={this.state.pluginNames[i]}
            onClick={async (e: React.MouseEvent<HTMLButtonElement>) => await this.handleClick(i)}
          />
        </div>
      )
    }
    return plugins
  }

  render (): JSX.Element {
    if (this.state.state === 'busy') {
      return (
        <div className='infotext'>
          <p>
            The object detection web API is <span>slow</span>. Please wait. Thank you for your patience.
          </p>
          <p>
            In average, it takes 0.3 seconds to process 1 image.
          </p>
          <p>
            The visualization webpage will pop up in your browser after receiving the response from web API.
          </p>
        </div>
      )
    } else if (this.state.state === 'finish') {
      return (
        <div className='infotext'>
          <p>
            The framework finishes running. The webpage of visualization is poped up in your browser. Click on the button below to use the framework again.
          </p>
          <button className='button' onClick={this.handleNewFramework}>
            Start Again
          </button>
        </div>
      )
    } else if (this.state.state === 'error') {
      return (
        <div className='infotext'>
          <p>
            Oops! There is something wrong about the web api. Here's what might happen:
          </p>
          <p>
            1. You add too many images in the data plugin.
          </p>
          <p>
            2. Your account is restricted because of frequent use (most likely).
          </p>
          <p>
            Solution: register a new account and acquire a new API key.
          </p>
          <p>
            Sorry for the inconvience. The web API is free, so your account could be restricted for frequent use.
          </p>
          <p>
            If you have difficulty registering a new account, please contact us: yifanz7@andrew.cmu.edu
          </p>
          <p>
            Click on the button below to use the framework again.
          </p>
          <button className='button' onClick={this.handleNewFramework}>
            Start Again
          </button>
        </div>
      )
    } else if (this.state.state === 'chooseDataPlugin') {
      return (
        <div className='infotext'>
          <p>
            Please choose a data plugin.
          </p>
          {this.renderPlugin()}
        </div>
      )
    } else {
      return (
        <div className='infotext'>
          <p>
            Please choose a visualization plugin.
          </p>
          {this.renderPlugin()}
        </div>
      )
    }
  }
}

export default App
